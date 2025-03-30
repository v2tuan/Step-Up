package com.stepup.service;

import com.stepup.dtos.requests.AddressDTO;
import com.stepup.entity.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressService {

    List<AddressDTO> getAddressesByUserId(Long userId);

    // Thêm hoặc cập nhật địa chỉ
    Address saveAddress(AddressDTO address);

    Optional<Address> findById(Long addressId);

    //  Xóa địa chỉ theo ID
    boolean deleteAddress(Long addressId);

    // Xóa tất cả địa chỉ của một người dùng
    void deleteAddressesByUserId(Long userId);

    boolean addressExistsById(Long addressId);

    // Kiểm tra người dùng có địa chỉ nào chưa
    boolean userHasAddresses(Long userId);

    Address updateAddress(Long Id, AddressDTO address);
}
