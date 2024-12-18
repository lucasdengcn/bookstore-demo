/* (C) 2024 */ 

package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.lock.DistributedLock;
import com.example.demo.bookstore.lock.DxLockInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Locks APIs")
@RestController
@RequestMapping(value = "/locks", produces = MediaType.APPLICATION_JSON_VALUE)
public class LockController {

    private final DistributedLock distributedLock;

    public LockController(DistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    @Operation(description = "acquire a dx lock")
    @PostMapping("/v1/acquire")
    @ResponseStatus(HttpStatus.OK)
    public String acquire() {
        Optional<String> acquire = distributedLock.acquire("abc", Duration.ofSeconds(20));
        return acquire.orElse("NULL");
    }

    @Operation(description = "get a dx lock")
    @GetMapping("/v1/info/{key}")
    @ResponseStatus(HttpStatus.OK)
    public DxLockInfo info(@PathVariable String key) {
        Optional<DxLockInfo> acquire = distributedLock.findInCache(key);
        return acquire.orElse(new DxLockInfo());
    }

    @Operation(description = "forceRelease a dx lock")
    @DeleteMapping("/v1/info/{key}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean release(@PathVariable String key) {
        distributedLock.forceRelease(key);
        return true;
    }
}
