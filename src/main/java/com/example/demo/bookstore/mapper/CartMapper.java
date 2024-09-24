package com.example.demo.bookstore.mapper;

import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.model.input.CartCreateInput;
import com.example.demo.bookstore.model.output.CartInfo;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

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
