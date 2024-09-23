package com.example.demo.bookstore.mapper;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookMapperTests {

    public static final String TITLE = "book ABC";
    public static final String AUTHOR = "James";
    public static final String CATEGORY = "Java";
    public static final BigDecimal PRICE = BigDecimal.valueOf(50.20);
    public static final int AMOUNT = 100;
    public static final int BOOK_ID = 1;

    @Autowired
    BookMapper bookMapper;

    @Test
    void test_create_input_to_book_entity() {
        // Prepare
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        //
        Book book = Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        //
        Book mapperBook = bookMapper.toBook(bookCreateInput);
        Assertions.assertEquals(book, mapperBook);
    }

    @Test
    void test_invalid_null_to_book_entity() {
        //
        Book mapperBook = bookMapper.toBook(null);
        Assertions.assertNull(mapperBook);
    }


    @Test
    void test_update_input_to_book_entity() {
        // Prepare
        BookUpdateInput bookUpdateInput = BookUpdateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        //
        Book book = Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                .id(BOOK_ID)
                .price(PRICE).amount(AMOUNT).build();
        //
        Book mapperBook = bookMapper.toBook(bookUpdateInput, BOOK_ID);
        Assertions.assertEquals(book, mapperBook);
    }

    @Test
    void test_update_null_to_book_entity() {
        //
        Book mapperBook = bookMapper.toBook(null, null);
        Assertions.assertNull(mapperBook);
    }

    @Test
    void test_update_null2_to_book_entity() {
        //
        Book mapperBook = bookMapper.toBook(null, 1);
        Assertions.assertNotNull(mapperBook);
        Assertions.assertNotNull(mapperBook.getId());
        Assertions.assertNull(mapperBook.getPrice());
        Assertions.assertNull(mapperBook.getTitle());
        Assertions.assertNull(mapperBook.getCategory());
    }


    @Test
    void test_book_to_bookInfo() {
        Book book = Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                .id(BOOK_ID)
                .price(PRICE).amount(AMOUNT).build();
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE).author(AUTHOR)
                .category(CATEGORY).price(PRICE)
                .id(BOOK_ID).amount(AMOUNT).build();
        //
        BookInfo bookMapperBookInfo = bookMapper.toBookInfo(book);
        Assertions.assertEquals(bookInfo, bookMapperBookInfo);
    }

    @Test
    void test_null_book_to_bookInfo() {
        //
        BookInfo bookMapperBookInfo = bookMapper.toBookInfo(null);
        Assertions.assertNull(bookMapperBookInfo);
    }

    @Test
    void test_books_to_bookInfos() {
        List<Book> bookList = List.of(
                Book.builder().title(TITLE).author(AUTHOR).category(CATEGORY)
                        .id(BOOK_ID)
                        .price(PRICE).amount(AMOUNT).build()
        );
        //
        List<BookInfo> bookInfoList = List.of(
                BookInfo.builder().title(TITLE).author(AUTHOR)
                        .category(CATEGORY).price(PRICE)
                        .id(BOOK_ID).amount(AMOUNT).build()
        );
        //
        List<BookInfo> bookMapperBookInfos = bookMapper.toBookInfos(bookList);
        //
        for (int i = 0; i < bookInfoList.size(); i++) {
            Assertions.assertEquals(bookInfoList.get(i), bookMapperBookInfos.get(i));
        }
    }

    @Test
    void test_null_books_to_bookInfos() {
        //
        List<BookInfo> bookMapperBookInfos = bookMapper.toBookInfos(null);
        Assertions.assertNull(bookMapperBookInfos);
    }

    @Test
    void test_empty_books_to_bookInfos() {
        //
        List<BookInfo> bookMapperBookInfos = bookMapper.toBookInfos(Lists.newArrayList());
        Assertions.assertTrue(bookMapperBookInfos.isEmpty());
    }

}