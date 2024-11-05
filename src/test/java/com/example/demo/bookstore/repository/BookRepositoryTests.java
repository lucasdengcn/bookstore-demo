package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.DemoTestsBase;
import com.example.demo.bookstore.entity.Book;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Optional;


class BookRepositoryTests extends DemoTestsBase {

    @Autowired
    BookRepository bookRepository;

    Faker faker = new Faker();

    Book bookInit = null;

    @BeforeEach
    public void setup(){
        bookInit = Book.builder().title(randomTitle())
                .author("James smith").category("JAVA")
                .active(true)
                .amount(100).price(BigDecimal.valueOf(50.20))
                .build();
        bookInit = bookRepository.save(bookInit);
        System.out.println(bookInit);
    }

    private String randomTitle() {
        return faker.lorem().characters(10, 100, true, true);
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
                .active(true)
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
        Book book = bookRepository.findById(bookInit.getId()).orElseThrow();
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
        Book book = bookRepository.findByTitle(bookInit.getTitle());
        System.out.println(book);
        //
        Assertions.assertNotNull(book);
        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals(bookInit.getAmount(), book.getAmount());
        Assertions.assertEquals(bookInit.getPrice(), book.getPrice());
        Assertions.assertTrue(book.isActive());
    }

    // test on find available books
    @Test
    public void test_find_available_books(){
        int pageIndex = 0; // zero-based
        int pageSize = 10;
        Pageable pageable = PageRequest.ofSize(pageSize).withPage(pageIndex).withSort(Sort.by(Sort.Order.desc("id")));
        Page<Book> bookList = bookRepository.findByActive(true, pageable);
        //
        System.out.println(bookList);
        //
        Assertions.assertFalse(bookList.isEmpty());
        bookList.forEach(book -> Assertions.assertTrue(book.isActive()));
    }

//    // test on delete a book
    @Test
    public void test_delete_book(){
        Book book = bookRepository.findByTitle(bookInit.getTitle());
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

    @Test
    public void test_incr_book_amount(){
        int bookUpdated = bookRepository.offsetAmount(bookInit.getId(), 10);
        //
        Assertions.assertEquals(1, bookUpdated);
        //
        Book book = bookRepository.findById(bookInit.getId()).orElseThrow();
        //
        Assertions.assertEquals(110, book.getAmount());
        Assertions.assertTrue(book.isActive());
    }

    @Test
    public void test_decr_book_amount(){
        int bookUpdated = bookRepository.offsetAmount(bookInit.getId(), -10);
        //
        Assertions.assertEquals(1, bookUpdated);
        //
        Book book = bookRepository.findById(bookInit.getId()).orElseThrow();
        //
        Assertions.assertEquals(90, book.getAmount());
        Assertions.assertTrue(book.isActive());
    }

    @Test
    public void test_oversell_book_amount(){
        int bookUpdated = bookRepository.offsetAmount(bookInit.getId(), -1 * bookInit.getAmount());
        //
        Assertions.assertEquals(1, bookUpdated);
        //
        Book book = bookRepository.findById(bookInit.getId()).orElseThrow();
        //
        Assertions.assertEquals(0, book.getAmount());
        Assertions.assertFalse(book.isActive());
    }
}