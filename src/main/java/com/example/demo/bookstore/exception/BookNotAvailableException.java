package com.example.demo.bookstore.exception;


import java.util.function.Supplier;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(Integer bookId) {
        super("Book Not Available, id: " + bookId);
    }

    /**
     *
     * @param id
     * @return
     */
    public static Supplier<BookNotAvailableException> Create(Integer id){
        return new Supplier<BookNotAvailableException>() {
            @Override
            public BookNotAvailableException get() {
                return new BookNotAvailableException(id);
            }
        };
    }

}
