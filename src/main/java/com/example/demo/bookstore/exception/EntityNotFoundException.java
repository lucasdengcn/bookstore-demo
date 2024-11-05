/* (C) 2024 */ 

package com.example.demo.bookstore.exception;

import java.util.function.Supplier;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     *
     * @param id
     * @return
     */
    public static Supplier<EntityNotFoundException> BookNotFound(Integer id) {
        return new Supplier<EntityNotFoundException>() {
            @Override
            public EntityNotFoundException get() {
                return new EntityNotFoundException("Book Not Found with id: " + id);
            }
        };
    }

    /**
     *
     * @param id
     * @return
     */
    public static Supplier<EntityNotFoundException> CartNotFound(Integer id) {
        return new Supplier<EntityNotFoundException>() {
            @Override
            public EntityNotFoundException get() {
                return new EntityNotFoundException("Cart Not Found with id: " + id);
            }
        };
    }
}
