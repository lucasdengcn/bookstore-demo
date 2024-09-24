package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByTitle(String title);

    Page<Book> findByIsActive(boolean isActive, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Book b set b.amount = b.amount + ?2, b.isActive=b.amount + ?2 > 0 where b.id = ?1")
    int offsetAmount(Integer id, Integer amount);
}
