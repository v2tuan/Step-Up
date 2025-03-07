package com.stepup.repository;

import com.stepup.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_Id(Long userId);

    //Xóa giỏ hàng của người dùng (khi xóa tài khoản)
    void deleteByUser_Id(Long userId);

}
