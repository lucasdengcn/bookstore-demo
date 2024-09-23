package com.example.demo.bookstore.mapper;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toBook(BookCreateInput input);

    Book toBook(BookUpdateInput input, Integer id);

    BookInfo toBookInfo(Book book);

    List<BookInfo> toBookInfos(List<Book> books);
}
