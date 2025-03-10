package com.stepup.controller;

import com.stepup.dtos.requests.OrderItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_detail")
public class OrderDetailController {
    @PostMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderItemDTO orderItemDTO) {

        return ResponseEntity.ok("created OrderItem");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") Long id, @RequestBody OrderItemDTO orderItemDTO) {
        return ResponseEntity.ok("updated OrderItem");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok("deleted OrderItem");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok("OrderDetail: " + id);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Order: " + id);
    }

}
