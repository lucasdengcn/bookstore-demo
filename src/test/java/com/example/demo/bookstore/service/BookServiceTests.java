package com.example.demo.bookstore.service;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.exception.EntityNotFoundException;
import com.example.demo.bookstore.mapper.BookMapper;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class BookServiceTests {

    public static final String TITLE = "book ABC";
    public static final String AUTHOR = "James";
    public static final String CATEGORY = "Java";
    public static final BigDecimal PRICE = BigDecimal.valueOf(50.20);
    public static final int AMOUNT = 100;
    public static final int BOOK_ID = 1;

    public static final String TITLE_V2 = "book ABC V2";
    public static final int AMOUNT_V2 = 110;
    public static final BigDecimal PRICE_V2 = BigDecimal.valueOf(50.50);
    public static final int PAGE_SIZE = 10;
    public static final int PAGE_NUMBER = 0;


    @Autowired
    BookService bookService;

    @MockBean
    BookMapper bookMapper;

    @MockBean
    BookRepository bookRepository;

    @Test
    void test_create_book() {
        // Prepare
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        //
        Book book = Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                .id(BOOK_ID)
                .price(PRICE).amount(AMOUNT).build();
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE).author(AUTHOR)
                .category(CATEGORY).price(PRICE)
                .id(BOOK_ID).amount(AMOUNT).build();
        //
        given(bookMapper.toBook(bookCreateInput)).willReturn(book);
        given(bookRepository.save(book)).willReturn(book);
        given(bookMapper.toBookInfo(book)).willReturn(bookInfo);
        //expect
        BookInfo bookInfoCreated = bookService.create(bookCreateInput);
        //assert
        Assertions.assertEquals(bookInfo, bookInfoCreated);
    }


    @Test
    void test_update_book(){
        // Prepare
        BookUpdateInput bookUpdateInput = BookUpdateInput.builder()
                .title(TITLE_V2).author(AUTHOR).category(CATEGORY)
                .price(PRICE_V2).amount(AMOUNT_V2).build();
        //
        Book book = Book.builder().title(TITLE_V2).author(AUTHOR).category(CATEGORY)
                .id(BOOK_ID)
                .price(PRICE_V2).amount(AMOUNT).build();
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE_V2).author(AUTHOR)
                .category(CATEGORY).price(PRICE_V2)
                .id(BOOK_ID).amount(AMOUNT_V2).build();
        //
        given(bookMapper.toBook(bookUpdateInput, BOOK_ID)).willReturn(book);
        given(bookRepository.save(book)).willReturn(book);
        given(bookMapper.toBookInfo(book)).willReturn(bookInfo);
        //expect
        BookInfo bookInfoUpdated = bookService.update(BOOK_ID, bookUpdateInput);
        //assert
        Assertions.assertEquals(bookInfo, bookInfoUpdated);
    }

    @Test
    void test_deActive_book(){
        // Prepare
        Book book = Book.builder()
                .id(BOOK_ID).isActive(false)
                .build();
        BookInfo bookInfo = BookInfo.builder()
                .isActive(false)
                .id(BOOK_ID).build();
        //
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.of(book));
        given(bookRepository.save(book)).willReturn(book);
        given(bookMapper.toBookInfo(book)).willReturn(bookInfo);
        //expect
        BookInfo bookInfoUpdated = bookService.updateStatus(BOOK_ID, false);
        //assert
        assertFalse(bookInfoUpdated.getIsActive());
    }

    @Test
    void test_active_book(){
        // Prepare
        Book book = Book.builder()
                .id(BOOK_ID).isActive(true)
                .build();
        BookInfo bookInfo = BookInfo.builder()
                .isActive(true)
                .id(BOOK_ID).build();
        //
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.of(book));
        given(bookRepository.save(book)).willReturn(book);
        given(bookMapper.toBookInfo(book)).willReturn(bookInfo);
        //expect
        BookInfo bookInfoUpdated = bookService.updateStatus(BOOK_ID, true);
        //assert
        assertTrue(bookInfoUpdated.getIsActive());
    }

    @Test
    void test_find_available_books(){
        // Prepare
        List<Book> bookList = List.of(
            Book.builder().title(TITLE_V2).author(AUTHOR).category(CATEGORY)
                    .id(BOOK_ID)
                    .price(PRICE_V2).amount(AMOUNT).build()
        );
        List<BookInfo> bookInfoList = List.of(
                BookInfo.builder().title(TITLE_V2).author(AUTHOR).category(CATEGORY)
                        .id(BOOK_ID)
                        .price(PRICE_V2).amount(AMOUNT).build()
        );
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE)
                .withPage(PAGE_NUMBER)
                .withSort(Sort.by(Sort.Order.desc("id")));
        //
        Page<Book> bookPage = new PageImpl(bookList, pageable, 1);
        given(bookRepository.findByIsActive(true, pageable)).willReturn(bookPage);
        given(bookMapper.toBookInfos(bookList)).willReturn(bookInfoList);
        //
        PageableOutput<BookInfo> availableBooks = bookService.findAvailableBooks(PAGE_NUMBER, PAGE_SIZE);
        //
        Assertions.assertFalse(availableBooks.getItems().isEmpty());
        Assertions.assertEquals(bookList.size(), availableBooks.getTotalItems());
        Assertions.assertFalse(availableBooks.isHasPreviousPage());
        Assertions.assertFalse(availableBooks.isHasNextPage());
    }

    @Test
    void test_offset_amount_success(){
        // Prepare
        Book book = Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                .id(BOOK_ID)
                .price(PRICE).amount(AMOUNT).build();
        // Given
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.of(book));
        given(bookRepository.offsetAmount(BOOK_ID, 1)).willReturn(1);
        //
        boolean offset = bookService.offsetAmounts(BOOK_ID, 1);
        //
        Assertions.assertTrue(offset);
    }

    @Test
    void test_offset_non_existing_book_will_throw_exception(){
        // Given
        given(bookRepository.findById(2)).willThrow(EntityNotFoundException.class);
        //
        Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bookService.offsetAmounts(2, 1);
            }
        });
    }

    @Test
    void test_find_book_success(){
        // Prepare
        Book book = Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                .id(BOOK_ID)
                .price(PRICE).amount(AMOUNT).build();
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE).author(AUTHOR)
                .category(CATEGORY).price(PRICE)
                .id(BOOK_ID).amount(AMOUNT).build();
        // Given
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.of(book));
        given(bookMapper.toBookInfo(book)).willReturn(bookInfo);
        //
        BookInfo bookInfoReturned = bookService.findById(BOOK_ID);
        //
        Assertions.assertEquals(bookInfo, bookInfoReturned);
    }

    @Test
    void test_find_non_existing_book_will_throw_exception(){
        // Given
        given(bookRepository.findById(2)).willThrow(EntityNotFoundException.class);
        //
        Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bookService.findById(2);
            }
        });
    }

}