package com.example.demo.bookstore.service;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.mapper.BookMapper;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // find available book

    // update book amounts

}
