package com.stepup.service.impl;

import com.stepup.dtos.requests.AddToCartDTO;
import com.stepup.entity.Cart;
import com.stepup.entity.CartItem;
import com.stepup.entity.ProductVariant;
import com.stepup.entity.User;
import com.stepup.repository.CartItemRepository;
import com.stepup.repository.CartRepository;
import com.stepup.repository.ProductVariantRepository;
import com.stepup.service.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository repo;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
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

    public List<CartItem> getCartItemByUser(User user) {
        // Lấy giỏ hàng của người dùng hoặc tạo mới nếu chưa có
        Cart cart = repo.findByUser_Id(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return repo.save(newCart);
        });

        return cartItemRepository.findByCart_Id(cart.getId());
    }

    public String addToCart(User user, AddToCartDTO addToCartDTO) {
        // Lấy giỏ hàng của người dùng hoặc tạo mới nếu chưa có
        Cart cart = repo.findByUser_Id(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return repo.save(newCart);
        });

        // Tìm sản phẩm theo ID
        Optional<ProductVariant> productVariantOpt = productVariantRepository.findById(addToCartDTO.getProductVariantId());
        if (productVariantOpt.isEmpty()) {
            return "Sản phẩm không tồn tại";
        }

        ProductVariant productVariant = productVariantOpt.get();

        // Kiểm tra tồn kho
        if (productVariant.getQuantity() < addToCartDTO.getQuantity()) {
            return "Số lượng trong kho không đủ";
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProductVariant(cart, productVariant);
        // Thêm sản phẩm vào giỏ hàng
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getCount() + addToCartDTO.getQuantity();

            // Kiểm tra lại số lượng tồn kho
            if (productVariant.getQuantity() < newQuantity) {
                return "Số lượng trong kho không đủ cho tổng số lượng đã chọn";
            }

            existingItem.setCount(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // Thêm mới nếu chưa tồn tại
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(productVariant)
                    .count(addToCartDTO.getQuantity())
                    .build();
            cartItemRepository.save(cartItem);
        }
        return "Thêm vào giỏ hàng thành công";
    }

    public String removeFromCart(User user, long cartItemId) {
        // Lấy giỏ hàng của người dùng hoặc tạo mới nếu chưa có
        Cart cart = repo.findByUser_Id(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return repo.save(newCart);
        });

        // Tìm Cart Item theo ID
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            return "Cart Item không tồn tại";
        }

        CartItem cartItem = optionalCartItem.get();
        cartItemRepository.deleteById(cartItem.getId());
        return "Xóa sản phẩm khỏi giỏ hàng thành công";
    }
}
