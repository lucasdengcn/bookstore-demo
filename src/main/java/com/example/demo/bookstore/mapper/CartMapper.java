/* (C) 2024 */ 

package com.example.demo.bookstore.mapper;

import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.model.input.CartCreateInput;
import com.example.demo.bookstore.model.output.CartInfo;
import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    /**
     * @param userId
     * @param cartInput
     * @param price
     * @return
     */
    Cart toCart(int userId, CartCreateInput cartInput, BigDecimal price);

    /**
     *
     * @param cart
     * @return
     */
    CartInfo toCartInfo(Cart cart);

    /**
     *
     * @param cartList
     * @return
     */
    List<CartInfo> toCartInfos(List<Cart> cartList);
}
