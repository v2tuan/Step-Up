package com.stepup.mapper;

import com.stepup.dtos.responses.OrderItemResponse;
import com.stepup.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderItemMapper {
    @Mapping(target = "title", expression = "java(getTitle(orderItem))")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
    default String getTitle(OrderItem orderItem) {
        return orderItem.getProductVariant().getProduct().getName();
    }

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItemList);
}
