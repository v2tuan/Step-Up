package com.stepup.controller;

import com.stepup.dtos.requests.AddToCartDTO;
import com.stepup.dtos.responses.CartItemRespone;
import com.stepup.entity.CartItem;
import com.stepup.entity.User;
import com.stepup.mapper.ICartItemMapper;
import com.stepup.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
