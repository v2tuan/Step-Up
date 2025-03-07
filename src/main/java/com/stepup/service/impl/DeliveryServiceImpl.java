package com.stepup.service.impl;

import com.stepup.entity.Delivery;
import com.stepup.repository.DeliveryRepository;
import com.stepup.service.IDeliveryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements IDeliveryService {
    private DeliveryRepository repo;
    @Override
    public List<Delivery> getAllDeliveries() {
        return repo.findAll();
    }

    @Override
    public Optional<Delivery> getDeliveryById(Long deliveryId) {
        return repo.findById(deliveryId);
    }

    @Override
    public Optional<Delivery> getDeliveryByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Delivery> getActiveDeliveries() {
        return repo.findByIsDeleteFalse();
    }

    @Override
    public Double getDeliveryPriceById(Long deliveryId) {
        return repo.findPriceById(deliveryId);
    }

    @Override
    public Delivery saveDelivery(Delivery delivery) {
        return repo.save(delivery);
    }

    @Override
    public void deleteDelivery(Long deliveryId) {
        repo.deleteById(deliveryId);
    }
}
