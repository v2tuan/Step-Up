package com.stepup.repository;

import com.stepup.entity.Cart;
import com.stepup.entity.CartItem;
import com.stepup.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //Tìm tất cả sản phẩm trong giỏ hàng của người dùng
    List<CartItem> findByCart_Id(Long cartId);

    // Xóa tất cả sản phẩm trong giỏ hàng (khi thanh toán)
    void deleteByCart_Id(Long cartId);

    // Tính tổng số lượng sản phẩm trong giỏ hàng
    @Query("SELECT SUM(ci.count) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Integer getTotalItemsInCart(@Param("cartId") Long cartId);
    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);


}
