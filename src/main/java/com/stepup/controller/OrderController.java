package com.stepup.controller;

import com.stepup.components.SecurityUtils;
import com.stepup.dtos.requests.OrderDTO;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Order;
import com.stepup.entity.User;
import com.stepup.service.IAddressService;
import com.stepup.service.IOderItemService;
import com.stepup.service.IOderService;
import com.stepup.service.IUserService;
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
    private final IOderService orderService;
    @Autowired
    private final IOderItemService oderItemService ;
    @Autowired
    private final IUserService userService;
    @Autowired
    private final IAddressService addressService;
    @Autowired
    private SecurityUtils securityUtils;

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

        // Gọi service để tạo đơn hàng
        Order order = orderService.createOrder(orderDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Created Order successfully with items")
                .data(order)
                .status(HttpStatus.OK)
                .build());
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


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable String userId) {
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("User Orders")
                .build());
    }
}
