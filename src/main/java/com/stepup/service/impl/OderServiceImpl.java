package com.stepup.service.impl;

import com.stepup.Enum.Status;
import com.stepup.dtos.requests.OrderDTO;
import com.stepup.entity.Order;
import com.stepup.repository.AddressRepository;
import com.stepup.repository.OderRepository;
import com.stepup.repository.UserRepository;
import com.stepup.service.IOderItemService;
import com.stepup.service.IOderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OderServiceImpl implements IOderService {
    private OderRepository repo;
    private UserRepository userRepo;
    private AddressRepository addressRepo;
    private IOderItemService oderItemService;

    @Override
    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return repo.findById(orderId);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(Status status) {
        return repo.findByStatus(status);
    }

    @Override
    public Order saveOrder(Order order) {
        return repo.save(order);
    }

    @Override
    public Order updateOrderStatus(Long orderId, Status status) {
        Optional<Order> orderOpt = repo.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return repo.save(order);
        }
        return null;
    }

    @Override
    public void deleteOrderById(Long orderId) {
        repo.deleteById(orderId);
    }

    @Override
    public Double getTotalRevenue() {
        return repo.getTotalRevenue();
    }

    @Override
    public Double getRevenueByStatus(Status status) {
        return repo.getRevenueByStatus(status);
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUser(userRepo.findById(orderDTO.getUserId()).orElse(null));
        order.setAddress(addressRepo.findById(orderDTO.getAddressId()).orElse(null));
        order.setStatus(Status.PENDING);
        order.setIsPaidBefore(orderDTO.getIsPaidBefore());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        // Lưu Order trước để có ID
        order = repo.save(order);

        // Gọi OrderItemService để tạo OrderItems
        oderItemService.createOrderItems(order, orderDTO.getOrderItems());

        return order;
    }

}
