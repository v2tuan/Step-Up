package com.stepup.service.impl;

import com.stepup.entity.User;
import com.stepup.repository.UserRepository;
import com.stepup.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private UserRepository repo ;

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return repo.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return repo.findByPhone(phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return repo.existsByPhone(phone);
    }

    @Override
    public User saveUser(User user) {
        return repo.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        repo.deleteById(userId);
    }

    @Override
    public List<User> getUsersByEmailActive(boolean isEmailActive) {
        return repo.findByIsEmailActive(isEmailActive);
    }

    @Override
    public List<User> getUsersByPhoneActive(boolean isPhoneActive) {
        return repo.findByIsPhoneActive(isPhoneActive);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return repo.findByRole(role);
    }
}
