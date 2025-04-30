package com.stepup.controller;

import com.stepup.components.SecurityUtils;
import com.stepup.dtos.requests.OrderDTO;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Order;
import com.stepup.entity.User;
import com.stepup.mapper.IOrderMapper;
import com.stepup.service.IAddressService;
import com.stepup.service.IOderItemService;
import com.stepup.service.impl.OrderServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private final IOderItemService oderItemService ;
    @Autowired
    private final IAddressService addressService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private IOrderMapper orderMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .message(errors.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

//        User loginUser = securityUtils.getLoggedInUser();

        try {
            // Gọi service để tạo đơn hàng
            Order order = orderService.createOrder(orderDTO);

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Created Order successfully with items")
                    .data(order.getId())
                    .status(HttpStatus.OK)
                    .build());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.OK)
                    .build());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO orderDTO, BindingResult result) {
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(errors.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Updated Order")
                .build());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOrder(@RequestBody OrderDTO orderDTO, BindingResult result) {
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(errors.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Deleted Order")
                .build());
    }

    @GetMapping("/me/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable String userId) {
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("User Orders")
                .build());
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<?> getOrders(@PathVariable long orderId) {
//        Order order = orderService.getOrderById(orderId).orElse(null);
//        return ResponseEntity.ok().body(ResponseObject.builder()
//                .message("User Orders")
//                .data(order)
//                .build());
//    }

    @GetMapping("/{orderStatus}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String orderStatus) {
        User loginUser = securityUtils.getLoggedInUser();
        List<Order> orderList = orderService.getOrderByUserAndStatus(loginUser, orderStatus);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("User Orders")
                .data(orderMapper.toOrderResponseList(orderList))
                .build());
    }
}
