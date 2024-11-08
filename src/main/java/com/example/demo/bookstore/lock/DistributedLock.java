/* (C) 2024 */ 

package com.example.demo.bookstore.lock;

import com.github.benmanes.caffeine.cache.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DistributedLock implements RemovalListener<String, DxLockInfo> {

    private final StringRedisTemplate redisTemplate;

    private final Cache<String, DxLockInfo> dxLockInfoCache;

    private final ScheduledExecutorService cleanUp = Executors.newScheduledThreadPool(1);
    private final Counter counterOnLockNew;
    private final Counter counterOnLockReleased;
    private final Counter counterOnLockRenew;
    private final AtomicInteger aliveLock = new AtomicInteger(0);
    //
    public DistributedLock(StringRedisTemplate redisTemplate, MeterRegistry registry) {
        this.redisTemplate = redisTemplate;
        this.counterOnLockNew = Counter.builder("dx_lock_acquired_total")
                .description("distributed lock number of created")
                .register(registry);
        this.counterOnLockReleased = Counter.builder("dx_lock_released_total")
                .description("distributed lock number of released")
                .register(registry);
        this.counterOnLockRenew = Counter.builder("dx_lock_renew_total")
                .description("distributed lock number of renew")
                .register(registry);
        //
        Gauge.builder("dx_lock_alive", new Supplier<Number>() {
                    @Override
                    public Number get() {
                        return aliveLock.get();
                    }
                })
                .description("distributed lock number of active")
                .register(registry);

        // Evict based on a varying expiration policy
        this.dxLockInfoCache = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, DxLockInfo>() {
                    public long expireAfterCreate(String key, DxLockInfo graph, long currentTime) {
                        return graph.getDuration().minusSeconds(2).toNanos();
                    }

                    public long expireAfterUpdate(
                            String key, DxLockInfo graph, long currentTime, long currentDuration) {
                        return currentDuration;
                    }

                    public long expireAfterRead(String key, DxLockInfo graph, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .removalListener(this)
                .maximumSize(100_000)
                .build();

        log.info("DxLock in Cache: {}", dxLockInfoCache);

        cleanUp.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        long estimatedSize = dxLockInfoCache.estimatedSize();
                        if (estimatedSize == 0) {
                            return;
                        }
                        log.trace("lock in cache size: {}", estimatedSize);
                        dxLockInfoCache.cleanUp();
                    }
                },
                1,
                1,
                TimeUnit.SECONDS);
    }

    private String getLockKey(String businessKey) {
        return "dx-lock:" + businessKey;
    }

    public Optional<DxLockInfo> findInCache(String businessKey) {
        String lockKey = getLockKey(businessKey);
        DxLockInfo present = dxLockInfoCache.getIfPresent(lockKey);
        if (null == present) return Optional.empty();
        return Optional.of(present);
    }

    /**
     * acquire the lock on key ${businessKey}
     *
     * @param businessKey
     * @param duration
     * @return transaction id via random value
     */
    public Optional<String> acquire(String businessKey, Duration duration) {
        String lockKey = getLockKey(businessKey);
        String txId = UUID.randomUUID() + ":" + businessKey;
        //
        Boolean response = redisTemplate.boundValueOps(lockKey).setIfAbsent(txId, duration);
        //
        if (null == response) {
            return Optional.empty();
        }
        if (response) {
            // monitor lock
            putLocal(duration, txId, businessKey);
            counterOnLockNew.increment();
            aliveLock.incrementAndGet();
            //
            return Optional.of(txId);
        }
        return Optional.empty();
    }

    private void putLocal(Duration duration, String txId, String businessKey) {
        DxLockInfo dxLockInfo = createDxLockInfo(duration, txId);
        dxLockInfoCache.put(businessKey, dxLockInfo);
    }

    private DxLockInfo createDxLockInfo(Duration duration, String txId) {
        return DxLockInfo.builder().duration(duration).txId(txId).build();
    }

    /**
     *
     * @param businessKey
     * @param txId
     * @return
     */
    public int ttl(String businessKey, String txId) {
        String lockKey = getLockKey(businessKey);
        Long expire = redisTemplate.boundValueOps(lockKey).getExpire();
        log.debug("ttl response: {}", expire);
        if (null == expire) {
            return -2;
        }
        return expire.intValue();
    }

    /**
     *
     * @param businessKey
     */
    public void forceRelease(String businessKey) {
        String lockKey = getLockKey(businessKey);
        Boolean result = redisTemplate.delete(lockKey);
        if (null != result && result) {
            removeLocal(businessKey);
            counterOnLockReleased.increment();
            aliveLock.decrementAndGet();
        }
    }

    private void removeLocal(String businessKey) {
        dxLockInfoCache.invalidate(businessKey);
    }

    /**
     * Release the lock with value ${txId} on key ${businessKey}
     *
     * @param businessKey
     * @param txId
     * @return
     */
    public boolean release(String businessKey, String txId) {
        String script =
                """
                if redis.call("exists", KEYS[1]) == 0 then
                    return -1
                end
                if redis.call("get",KEYS[1]) == ARGV[1] then
                    return redis.call("del", KEYS[1])
                else
                    return 0
                end
                """;
        String lockKey = getLockKey(businessKey);
        // if key not exists (auto removed by redis) return -1
        // if value not match return 0
        // if key exits and value match return 1
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, List.of(lockKey), txId);
        //
        log.debug("release response: {}, {}, {}", businessKey, txId, result);
        if (null == result) {
            removeLocal(lockKey);
            return true;
        }
        boolean success = result.intValue() != 0;
        if (success) {
            removeLocal(lockKey);
            counterOnLockReleased.increment();
            aliveLock.decrementAndGet();
        }
        return success;
    }

    /**
     * extend a lock's duration
     *
     * @param businessKey
     * @param txId
     * @param duration
     * @return
     */
    public boolean renew(String businessKey, String txId, Duration duration) {
        String script =
                """
                if redis.call("get",KEYS[1]) == ARGV[1] then
                    return redis.call("expire", KEYS[1], ARGV[2])
                else
                    return 0
                end
                """;
        String lockKey = getLockKey(businessKey);
        //
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, List.of(lockKey), txId, String.valueOf(duration.toSeconds()));
        //
        log.debug("renew response: {}, {}, {}", lockKey, txId, result);
        if (null == result) {
            return true;
        }
        boolean success = result.intValue() > 0;
        if (success) {
            putLocal(duration, txId, lockKey);
            counterOnLockRenew.increment();
        }
        return success;
    }

    @Override
    public void onRemoval(@Nullable String key, @Nullable DxLockInfo value, RemovalCause cause) {
        assert value != null;
        // auto extend lock duration
        log.debug("onRemoval: {}, {}, {}", key, value, cause.wasEvicted());
        if (cause.wasEvicted()) {
            renew(key, value.getTxId(), value.getDuration());
        }
    }
}
