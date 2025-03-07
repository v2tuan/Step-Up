package com.stepup.repository;

import com.stepup.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository   extends JpaRepository<Address, Long> {
    // Tìm tất cả địa chỉ của người dùng
    List<Address> findByUser_Id(Long userId);

    //  Xóa tất cả địa chỉ của một người dùng
    void deleteByUser_Id(Long userId);

    //  Kiểm tra xem người dùng có địa chỉ nào chưa
    boolean existsByUser_Id(Long userId);

}
