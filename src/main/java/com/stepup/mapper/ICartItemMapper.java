package com.stepup.mapper;

import com.stepup.dtos.responses.CartItemRespone;
import com.stepup.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartItemMapper {
    @Mapping(target = "title", expression = "java(getTitle(cartItem))")
    CartItemRespone toCartItemRespone(CartItem cartItem); // ánh xạ từng CartItem

    List<CartItemRespone> toCartItemRespone(List<CartItem> cartItems);

    default String getTitle(CartItem cartItem) {
        return cartItem.getProductVariant().getProduct().getName();
    }
}
