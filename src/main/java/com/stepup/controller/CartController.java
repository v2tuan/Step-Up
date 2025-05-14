package com.stepup.controller;

import com.stepup.Enum.OrderShippingStatus;
import com.stepup.Enum.PaymentStatus;
import com.stepup.dtos.requests.AddToCartDTO;
import com.stepup.dtos.responses.CartItemRespone;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.CartItem;
import com.stepup.entity.Order;
import com.stepup.entity.User;
import com.stepup.mapper.ICartItemMapper;
import com.stepup.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private ICartItemMapper cartItemMapper;
    @GetMapping
    public List<CartItemRespone> getAllCartItem(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            return cartItemMapper.toCartItemRespone(cartService.getCartItemByUser(user));
        } else {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống"); // Trường hợp không có người dùng đăng nhập
        }
    }
    @PostMapping("/add")
    public String addCart(@RequestBody AddToCartDTO addToCartDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            return cartService.addToCart(user, addToCartDTO);
        } else {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống"); // Trường hợp không có người dùng đăng nhập
        }
    }

    @PostMapping("/remove/{id}")
    public String removeCartItem(@PathVariable("id") long cartItemId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            return cartService.removeFromCart(user, cartItemId);
        } else {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống"); // Trường hợp không có người dùng đăng nhập
        }
    }
    @PostMapping("/cartFromOrder")
    public String AddCartFromOrder(@RequestBody List<AddToCartDTO> addToCartDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            StringBuilder result = new StringBuilder();
            for (AddToCartDTO dto : addToCartDTO) {
                String message = cartService.addToCart(user, dto);
                result.append(message).append("\n");
            }
            return result.toString();
        } else {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống"); // Trường hợp không có người dùng đăng nhập
        }
    }


}
