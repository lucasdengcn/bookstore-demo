package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    /**
     *
     * @param userId
     * @param bookId
     * @return
     */
    Cart findByUserIdAndBookId(int userId, Integer bookId);

    /**
     *
     * @param id
     * @param amount
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Cart c set c.amount = c.amount + ?2, c.total = c.price * (c.amount + ?2) where c.id = ?1")
    int offsetAmount(Integer id, int amount);

    /**
     *
     * @param userId
     * @return
     */
    List<Cart> findByUserId(int userId);


    @Query("select sum(c.total) from Cart c where c.userId = ?1")
    BigDecimal getTotalPrice(int userId);

}
