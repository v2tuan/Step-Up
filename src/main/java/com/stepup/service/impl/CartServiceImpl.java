package com.stepup.service.impl;

import com.stepup.entity.Cart;
import com.stepup.repository.CartRepository;
import com.stepup.service.ICartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    private CartRepository repo;
    @Override
    public Optional<Cart> getCartByUserId(Long userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return repo.save(cart);
    }

    @Override
    public void deleteCartByUserId(Long userId) {
        repo.deleteByUser_Id(userId);
    }
}
