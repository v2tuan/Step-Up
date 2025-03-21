package com.stepup.service.impl;

import com.stepup.dtos.requests.UserDTO;
import com.stepup.dtos.requests.VerifyAccountDTO;
import com.stepup.entity.User;
import com.stepup.mapper.IUserMapper;
import com.stepup.repository.UserRepository;
import com.stepup.service.EmailService;
import com.stepup.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IUserMapper userMapper;

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

    public User createUser(UserDTO userDTO){
        User newUser = userMapper.toUser(userDTO);
        newUser.setVerificationCode(generateVerificationCode());
        newUser.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        newUser.setEnabled(false);

        sendVerificationEmail(newUser);
        return userRepository.save(newUser);
    }

    public User verifyUser(VerifyAccountDTO input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


}
