package com.stepup.service;

import com.stepup.entity.Delivery;

import java.util.List;
import java.util.Optional;

public interface IDeliveryService {
    //  Lấy tất cả phương thức giao hàng
    List<Delivery> getAllDeliveries();

    // Lấy phương thức giao hàng theo ID
    Optional<Delivery> getDeliveryById(Long deliveryId);

    // Lấy phương thức giao hàng theo tên
    Optional<Delivery> getDeliveryByName(String name);

    //  Lấy danh sách phương thức giao hàng chưa bị xóa
    List<Delivery> getActiveDeliveries();


    //  Lấy giá của phương thức giao hàng theo ID
    Double getDeliveryPriceById(Long deliveryId);

    // Thêm hoặc cập nhật phương thức giao hàng
    Delivery saveDelivery(Delivery delivery);

    // Xóa phương thức giao hàng
    void deleteDelivery(Long deliveryId);
}
