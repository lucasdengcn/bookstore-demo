/* (C) 2024 */ 

package com.example.demo.bookstore.exception;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(Integer bookId) {
        super("Book Not Available, id: " + bookId);
    }

    //    /**
    //     *
    //     * @param id
    //     * @return
    //     */
    //    public static Supplier<BookNotAvailableException> Create(Integer id){
    //        return new Supplier<BookNotAvailableException>() {
    //            @Override
    //            public BookNotAvailableException get() {
    //                return new BookNotAvailableException(id);
    //            }
    //        };
    //    }

}
