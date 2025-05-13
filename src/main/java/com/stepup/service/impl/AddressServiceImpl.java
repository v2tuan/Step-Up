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
    public Address saveAddress(AddressDTO addressDTO, User user) {
        Address address = new Address();
        address.setPhone(addressDTO.getPhone());
        address.setAddr(addressDTO.getAddr());
        address.setFullName(addressDTO.getFullName());
        address.setUser(user);
        // Lưu vào repository
        return repo.save(address);

    }

    @Override
    public Optional<Address> findById(Long addressId) {
        return repo.findById(addressId);
    }

    @Override
    public boolean deleteAddress(Long addressId,User user) {
        if (repo.existsById(addressId)) {
            if (user.getDefaultAddress() != null && user.getDefaultAddress().getId().equals(addressId)) {
                user.setDefaultAddress(null);
                userRepo.save(user);
            }
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
        address.setAddr(updatedAddress.getAddr());
        address.setFullName(updatedAddress.getFullName());
        return repo.save(address);
    }

    public boolean setDefaultAddress(Long addressId, User user) {
        Address address = repo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + addressId));
        user.setDefaultAddress(address);
        userRepo.save(user);
        return true;
    }
    public boolean UnSetDefaultAddress(Long addressId,User user) {
        if(user.getDefaultAddress() != null && user.getDefaultAddress().getId().equals(addressId)) {
            user.setDefaultAddress(null);
            userRepo.save(user);
        }
        return true;
    }

//    public Address getDefaultAddress(Long userId) {
//        List<Address> addresses = repo.findByUser_Id(userId);
//        for (Address address : addresses) {
//            if(address.get)
//        }
//    }
}
