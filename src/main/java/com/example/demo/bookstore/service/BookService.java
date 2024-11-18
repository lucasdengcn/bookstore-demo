/* (C) 2024 */ 

package com.example.demo.bookstore.service;

import com.example.demo.bookstore.configuration.BookstoreProperties;
import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.event.BookAddedIntoCart;
import com.example.demo.bookstore.event.BookRemovedFromCart;
import com.example.demo.bookstore.exception.EntityNotFoundException;
import com.example.demo.bookstore.mapper.BookMapper;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.repository.BookRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookstoreProperties bookstoreProperties;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, BookstoreProperties bookstoreProperties) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookstoreProperties = bookstoreProperties;
    }

    // create a book
    public BookInfo create(BookCreateInput input) {
        log.debug("create book: {}", input);
        Book book = bookMapper.toBook(input);
        book.setActive(true);
        book = bookRepository.save(book);
        return bookMapper.toBookInfo(book);
    }

    // update a book
    // @CacheEvict(value = "books", key = "#id")
    public BookInfo update(Integer id, BookUpdateInput input) {
        log.debug("update book: {}, {}", id, input);
        Book book = bookMapper.toBook(input, id);
        book = bookRepository.save(book);
        return bookMapper.toBookInfo(book);
    }

    // update a book status only
    // @CachePut(value = "books", key = "#id")
    public BookInfo updateStatus(Integer id, boolean active) {
        log.debug("update book status: {}, {}", id, active);
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException.BookNotFound(id));
        book.setActive(active);
        book = bookRepository.save(book);
        return bookMapper.toBookInfo(book);
    }

    /**
     *
     * @param page zero-based
     * @param size
     * @return
     */
    public PageableOutput<BookInfo> findAvailableBooks(int page, int size) {
        //
        Pageable pageable = PageRequest.ofSize(size).withPage(page).withSort(Sort.by(Sort.Order.desc("id")));
        Page<Book> bookList = bookRepository.findByActive(true, pageable);
        //
        List<BookInfo> bookInfoList = bookMapper.toBookInfos(bookList.toList());
        return new PageableOutput<BookInfo>(bookInfoList, bookList, page, size);
    }

    /**
     * Offset book's amount
     * @param id
     * @param amount gte 0 means incr, lte 0 means decr.
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public boolean offsetAmounts(Integer id, Integer amount) throws EntityNotFoundException {
        log.info("try to offset amount of book id={}, change={}", id, amount);
        int result = bookRepository.offsetAmount(id, amount);
        return result > 0;
    }

    // get book detail
    // @Cacheable(cacheNames = "books", key = "#id")
    public BookInfo findById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException.BookNotFound(id));
        return bookMapper.toBookInfo(book);
    }

    @EventListener
    public void onBookAddedIntoCartEvent(BookAddedIntoCart event) {
        log.info("Receive added event: {}", event);
        if (null != event) {
            if (event.getBookId() > 0 && event.getAmount() > 0) {
                offsetAmounts(event.getBookId(), -1 * event.getAmount());
            }
        }
    }

    @EventListener
    public void onBookRemovedFromCartEvent(BookRemovedFromCart event) {
        log.info("Receive removed event: {}", event);
        if (null != event) {
            if (event.getBookId() > 0 && event.getAmount() > 0) {
                offsetAmounts(event.getBookId(), event.getAmount());
            }
        }
    }

    public List<BookInfo> findByIds(Set<Integer> bookIds) {
        if (null == bookIds || bookIds.isEmpty()) {
            return Collections.emptyList();
        }
        //
        List<Book> bookList = bookRepository.findAllById(bookIds);
        return bookMapper.toBookInfos(bookList);
    }

    @Transactional
    public BookInfo create(BookCreateInput input, MultipartFile file) {
        log.debug("create book with file: {}", input);
        Book book = bookMapper.toBook(input);
        book.setActive(true);
        // save file to directory
        try {
            String directory = bookstoreProperties.getRelativePath() + file.getOriginalFilename();
            Path outputPath = new File(directory).toPath();
            Files.copy(file.getInputStream(), outputPath);
            book.setCoverImageUrl(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //
        book = bookRepository.save(book);
        return bookMapper.toBookInfo(book);
    }
}
