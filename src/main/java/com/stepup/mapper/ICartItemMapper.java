package com.stepup.mapper;

import com.stepup.dtos.responses.CartItemRespone;
import com.stepup.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartItemMapper {
    List<CartItemRespone> toCartItemRespone(List<CartItem> cartItems);
}
