package com.stepup.service.impl;

import com.stepup.entity.OrderItem;
import com.stepup.repository.OderItemRepository;
import com.stepup.service.IOderItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OderItemServiceImpl implements IOderItemService {
    private OderItemRepository repo;

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return repo.findByOrder_Id(orderId);
    }

    @Override
    public Optional<OrderItem> getOrderItemById(Long orderItemId) {
        return repo.findById(orderItemId);
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return repo.save(orderItem);
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        repo.deleteById(orderItemId);
    }

    @Override
    public void deleteOrderItemsByOrderId(Long orderId) {
       repo.deleteByOrder_Id(orderId);
    }

    @Override
    public Double getTotalPriceByOrderId(Long orderId) {
        return 0.0;
    }

    @Override
    public Double getTotalDiscountedPriceByOrderId(Long orderId) {
        return 0.0;
    }
}
