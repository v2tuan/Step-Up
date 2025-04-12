package com.stepup.service.impl;

import com.stepup.dtos.requests.OrderItemDTO;
import com.stepup.entity.Order;
import com.stepup.entity.OrderItem;
import com.stepup.repository.DeliveryRepository;
import com.stepup.repository.OderItemRepository;
import com.stepup.repository.ProductVariantRepository;
import com.stepup.service.IOderItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OderItemServiceImpl implements IOderItemService {
    private OderItemRepository repo;
    private ProductVariantRepository productVariantRepository;
    private DeliveryRepository deliveryRepository;

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

    @Override
    public List<OrderItem> createOrderItems(Order order, List<OrderItemDTO> orderItemDTOs) {
        List<OrderItem> orderItems = orderItemDTOs.stream().map(itemDTO -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductVariant(productVariantRepository.findById(itemDTO.getProductVariantId()).orElse(null));
            orderItem.setCount(itemDTO.getCount());
//            orderItem.setPrice(itemDTO.getPrice());
//            orderItem.setDelivery(deliveryRepository.findById(itemDTO.getDeliveryId()).orElse(null));
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());
            return orderItem;
        }).toList();

        return repo.saveAll(orderItems);
    }
}
