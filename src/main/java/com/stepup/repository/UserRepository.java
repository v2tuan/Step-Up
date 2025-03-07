package com.stepup.repository;

import com.stepup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findByRole(String role);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByIsEmailActive(boolean isEmailActive);
    List<User> findByIsPhoneActive(boolean isPhoneActive);

}
