package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByTitle(String title);

    Page<Book> findByIsActive(boolean isActive, Pageable pageable);

}
