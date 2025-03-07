package com.stepup.repository;

import com.stepup.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

  //  Lấy tất cả phương thức giao hàng (mặc định của JpaRepository)
    List<Delivery> findAll();
    //  Tìm phưong thức giao hàng theo tên
    Optional<Delivery> findByName(String name);

    // Lấy danh sách phương thức giao hàng đang hoạt động
    List<Delivery> findByIsDeleteFalse();

    //  Lấy giá của phương thức giao hàng theo ID
    @Query("SELECT d.price FROM Delivery d WHERE d.id = :deliveryId")
    Double findPriceById(@Param("deliveryId") Long deliveryId);
}
