package com.stepup.service;

import com.stepup.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemService {
    //Lấy danh sách sản phẩm trong giỏ hàng
    List<CartItem> getCartItemsByCartId(Long cartId);

    // Thêm hoặc cập nhật sản phẩm trong giỏ hàng
    CartItem saveCartItem(CartItem cartItem);

    // Xóa sản phẩm khỏi giỏ hàng
    void deleteCartItem(Long cartItemId);

    // Xóa tất cả sản phẩm trong giỏ hàng
    void deleteCartItemsByCartId(Long cartId);

    // Tính tổng số lượng sản phẩm trong giỏ hàng
    Integer getTotalItemsInCart(Long cartId);

    // Tính tổng tiền giỏ hàng (không tính khuyến mãi)
    Double getTotalPriceInCart(Long cartId);

    // Tính tổng tiền giỏ hàng (có tính khuyến mãi)
    Double getTotalDiscountedPriceInCart(Long cartId);
}
