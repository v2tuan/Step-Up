package com.stepup.service;

import com.stepup.entity.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressService {

    List<Address> getAddressesByUserId(Long userId);

    // Thêm hoặc cập nhật địa chỉ
    Address saveAddress(Address address);

    //  Xóa địa chỉ theo ID
    void deleteAddress(Long addressId);

    // Xóa tất cả địa chỉ của một người dùng
    void deleteAddressesByUserId(Long userId);
}
