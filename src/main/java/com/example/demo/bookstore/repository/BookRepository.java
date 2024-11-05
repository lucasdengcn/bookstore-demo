/* (C) 2024 */ 

package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByTitle(String title);

    Page<Book> findByActive(boolean active, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Book b set b.amount = b.amount + ?2, b.active=b.amount + ?2 > 0 where b.id = ?1")
    int offsetAmount(Integer id, Integer amount);
}
