package com.example.demo.bookstore.service;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.event.BookAddedIntoCart;
import com.example.demo.bookstore.event.BookRemovedFromCart;
import com.example.demo.bookstore.mapper.BookMapper;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    // create a book
    public BookInfo create(BookCreateInput input){
        return null;
    }

    // create a book
    public BookInfo update(Integer id, BookCreateInput input){
        return null;
    }

    // find available book
    public PageableOutput<BookInfo> findAvailableBooks(int page, int size){
        return null;
    }

    // update book amounts
    public BookInfo updateAmounts(Integer id, Integer amount) {
        return null;
    }

    // get book detail
    public BookInfo findById(Integer id){
        return null;
    }

    @Async
    @TransactionalEventListener
    public void onBookAddedIntoCartEvent(BookAddedIntoCart event){

    }

    @Async
    @TransactionalEventListener
    public void onBookRemovedFromCartEvent(BookRemovedFromCart event){

    }

}
