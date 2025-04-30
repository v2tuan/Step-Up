package com.stepup.mapper;

import com.stepup.dtos.responses.OrderItemResponse;
import com.stepup.dtos.responses.OrderResponse;
import com.stepup.entity.Order;
import com.stepup.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = IOrderItemMapper.class)
public interface IOrderMapper {
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(List<Order> orders);
}