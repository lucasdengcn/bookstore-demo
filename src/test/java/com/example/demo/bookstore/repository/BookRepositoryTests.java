package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.entity.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class BookRepositoryTests {

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void setup(){
        //
        Book book = Book.builder().title("book ABC")
                .author("James smith").category("JAVA")
                .isActive(true)
                .amount(100).price(BigDecimal.valueOf(50.20))
                .build();
        bookRepository.save(book);
    }

    @AfterEach
    public void deleteAll(){
        bookRepository.deleteAll();
    }


    // test on create a book
    @Test
    public void test_create_book(){
        Book book = Book.builder().title("book ABC V2")
                .author("James smith").category("JAVA")
                .isActive(true)
                .amount(100).price(BigDecimal.valueOf(50.20))
                .build();
        //
        bookRepository.save(book);
        //
        Assertions.assertNotNull(book.getId());
        Assertions.assertTrue(book.getAmount() > 0);
        Assertions.assertTrue(book.isActive());
    }

    // test on create a book
    @Test
    public void test_deActive_book(){
        Book book = bookRepository.findById(1).orElseThrow();
        //
        book.setActive(false);
        Book bookUpdated = bookRepository.save(book);
        //
        Assertions.assertNotNull(bookUpdated.getId());
        Assertions.assertFalse(bookUpdated.isActive());
        Assertions.assertNotNull(bookUpdated.getPrice());
        Assertions.assertTrue(bookUpdated.getAmount() > 0);
    }

    // test on find a book
    @Test
    public void test_find_book(){
        Book book = bookRepository.findByTitle("book ABC");
        System.out.println(book);
        //
        Assertions.assertNotNull(book);
        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals(100, book.getAmount());
        Assertions.assertEquals(BigDecimal.valueOf(50.20), book.getPrice());
        Assertions.assertTrue(book.isActive());
    }

    // test on find available books
    @Test
    public void test_find_available_books(){
        int pageIndex = 0; // zero-based
        int pageSize = 10;
        Pageable pageable = PageRequest.ofSize(pageSize).withPage(pageIndex).withSort(Sort.by(Sort.Order.desc("id")));
        Page<Book> bookList = bookRepository.findByIsActive(true, pageable);
        //
        System.out.println(bookList);
        //
        Assertions.assertFalse(bookList.isEmpty());
        bookList.forEach(book -> Assertions.assertTrue(book.isActive()));
    }

    // test on delete a book
    @Test
    public void test_delete_book(){
        Book book = bookRepository.findByTitle("book ABC");
        //
        Assertions.assertNotNull(book);
        //
        bookRepository.deleteById(book.getId());
        //
        Optional<Book> deletedBook = bookRepository.findById(book.getId());
        Assertions.assertFalse(deletedBook.isPresent());
    }

    // test on find a non-existing book
    @Test
    public void test_find_non_existing_book(){
        Book book = bookRepository.findByTitle("NON_EXISTING_BOOK");
        Assertions.assertNull(book);
    }


}