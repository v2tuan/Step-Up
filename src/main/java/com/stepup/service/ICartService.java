package com.stepup.service;
import com.stepup.entity.Cart;

import java.util.Optional;

public interface ICartService {
    // Lấy giỏ hàng của người dùng
    Optional<Cart> getCartByUserId(Long userId);


    // Lưu hoặc cập nhật giỏ hàng
    Cart saveCart(Cart cart);

    // Xóa giỏ hàng của người dùng
    void deleteCartByUserId(Long userId);
}
