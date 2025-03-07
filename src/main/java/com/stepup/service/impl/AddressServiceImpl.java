package com.stepup.service.impl;

import com.stepup.entity.Address;
import com.stepup.repository.AddressRepository;
import com.stepup.service.IAddressService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AddressServiceImpl implements IAddressService {
    private AddressRepository repo;
    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public Address saveAddress(Address address) {
        return repo.save(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        repo.deleteById(addressId);
    }

    @Override
    public void deleteAddressesByUserId(Long userId) {
        repo.deleteByUser_Id(userId);
    }
}
