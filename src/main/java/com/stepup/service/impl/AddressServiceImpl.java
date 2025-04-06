package com.stepup.service.impl;

import com.stepup.entity.User;
import com.stepup.service.IAddressService;

import com.stepup.dtos.requests.AddressDTO;
import com.stepup.entity.Address;
import com.stepup.repository.AddressRepository;
import com.stepup.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl  implements IAddressService {

    @Autowired
    private AddressRepository repo;
    @Autowired
    private UserRepository userRepo;

    @Override
    public List<AddressDTO> getAddressesByUserId(Long userId) {
        List<Address> addresses = repo.findByUser_Id(userId);
        return addresses.stream()
                .map(addr -> new AddressDTO(addr.getId(),addr.getFullName(), addr.getPhone(), addr.getAddr(), addr.getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Address saveAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setPhone(addressDTO.getPhone());
        address.setAddr(addressDTO.getAddress());
        address.setFullName(addressDTO.getFullName());

        // Tìm user từ userRepo
        userRepo.findById(addressDTO.getUserId()).ifPresentOrElse(
                user -> {
                    address.setUser(user);// Set thêm full_name từ User
                },
                () -> {
                    throw new EntityNotFoundException("Không tìm thấy User với ID: " + addressDTO.getUserId());
                }
        );
        // Lưu vào repository
        return repo.save(address);

    }

    @Override
    public Optional<Address> findById(Long addressId) {
        return repo.findById(addressId);
    }

    @Override
    public boolean deleteAddress(Long addressId) {
        if (repo.existsById(addressId)) {
            repo.deleteById(addressId);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAddressesByUserId(Long userId) {
        repo.deleteByUser_Id(userId);
    }

    @Override
    public boolean addressExistsById(Long addressId) {
        return repo.existsById(addressId);
    }

    @Override
    public boolean userHasAddresses(Long userId) {
        return repo.existsByUser_Id(userId);

    }

    @Override
    public Address updateAddress(Long id, AddressDTO updatedAddress) {
        Address address = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Address với ID: " + id));
        updatedAddress.setId(id);
        address.setPhone(updatedAddress.getPhone());
        address.setAddr(updatedAddress.getAddress());
        address.setFullName(updatedAddress.getFullName());
        return repo.save(address);
    }

    public boolean setDefaultAddress(Long addressId, long UserID) {
        Address address = repo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + addressId));
        User user = userRepo.findById(UserID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        // Đặt địa chỉ mặc định
        user.setDefaultAddress(address);
        userRepo.save(user);
        return true;
    }
}
