package com.stepup.service.impl;
import com.stepup.entity.CartItem;
import com.stepup.repository.CartItemRepository;
import com.stepup.service.ICartItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements ICartItemService {

    private CartItemRepository repo ;
    @Override
    public List<CartItem> getCartItemsByCartId(Long cartId) {
        return repo.findByCart_Id(cartId);
    }


    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        return repo.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        repo.deleteById(cartItemId);
    }

    @Override
    public void deleteCartItemsByCartId(Long cartId) {
        repo.deleteByCart_Id(cartId);
    }

    @Override
    public Integer getTotalItemsInCart(Long cartId) {
        return repo.getTotalItemsInCart(cartId);
    }

    @Override
    public Double getTotalPriceInCart(Long cartId) {
        return 0.0;
    }

    @Override
    public Double getTotalDiscountedPriceInCart(Long cartId) {
        return 0.0;
    }
}
