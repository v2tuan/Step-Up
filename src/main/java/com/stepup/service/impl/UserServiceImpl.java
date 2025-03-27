package com.stepup.service.impl;

import com.stepup.Enum.Role;
import com.stepup.dtos.requests.UserDTO;
import com.stepup.dtos.requests.UserLoginDTO;
import com.stepup.dtos.requests.VerifyAccountDTO;
import com.stepup.entity.User;
import com.stepup.mapper.IUserMapper;
import com.stepup.repository.UserRepository;
import com.stepup.service.EmailService;
import com.stepup.service.IUserService;
import com.stepup.utils.JwtTokenUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
        // Kiểm tra xem email của tài khoản đã tồn tại trong hệ thống chưa
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trên một tài khoản khác.");
        }
        User newUser = userMapper.toUser(userDTO);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        newUser.setVerificationCode(generateVerificationCode());
        newUser.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        newUser.setEnabled(false);
        newUser.setRole(Role.CUSTOMER);
        sendVerificationEmail(newUser);
        return userRepository.save(newUser);
    }

    public String login(String email, String password){
        Optional<User> optionalUserEntity = userRepository.findByEmail(email);

        if (optionalUserEntity.isEmpty()) {
            throw new RuntimeException("Invalid Email");
        }

        // Bước 3: Lấy tài khoản người dùng hiện tại từ đối tượng Optional.
        User user = optionalUserEntity.get();

        // Bước 4: Kiểm tra mật khẩu người dùng nhập vào có khớp với mật khẩu đã được mã hóa trong cơ sở dữ liệu hay không.
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong Password"); // Nếu không khớp, ném ngoại lệ "Mật khẩu sai".
        }

        // Bước 5: Tạo đối tượng authenticationToken để chứa thông tin xác thực, bao gồm identifier (email/username) và các quyền của người dùng.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                user.getAuthorities() // Các quyền hạn của người dùng.
        );

        // Bước 6: Xác thực thông qua authenticationManager để kiểm tra tính hợp lệ của người dùng.
        authenticationManager.authenticate(authenticationToken);

        // Bước 7: Tạo và trả về JWT token cho người dùng sau khi xác thực thành công.
        return jwtTokenUtil.generateToken(user);
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


    public String loginSocial(UserLoginDTO userLoginDTO) throws Exception {
        Optional<User> optionalUser = Optional.empty();
//        Role roleUser = roleRepository.findByName(Role.USER)
//                .orElseThrow(() -> new DataNotFoundException(
//                        localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS)));

        // Kiểm tra Google Account ID
        if (userLoginDTO.isGoogleAccountIdValid()) {
            optionalUser = userRepository.findByGoogleAccountId(userLoginDTO.getGoogleAccountId());

            // Tạo người dùng mới nếu không tìm thấy
            if (optionalUser.isEmpty()) {
                User newUser = User.builder()
                        .fullName(Optional.ofNullable(userLoginDTO.getFullname()).orElse(""))
                        .email(Optional.ofNullable(userLoginDTO.getEmail()).orElse(""))
                        .profileImage(Optional.ofNullable(userLoginDTO.getProfileImage()).orElse(""))
                        .role(Role.CUSTOMER)
                        .googleAccountId(userLoginDTO.getGoogleAccountId())
                        .password("") // Mật khẩu trống cho đăng nhập mạng xã hội
                        .enabled(true)
                        .build();

                // Lưu người dùng mới
                newUser = userRepository.save(newUser);
                optionalUser = Optional.of(newUser);
            }
        } else {
            throw new IllegalArgumentException("Invalid social account information.");
        }

        User user = optionalUser.get();

        // Kiểm tra nếu tài khoản bị khóa
//        if (!user.isActive()) {
//            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
//        }

        // Tạo JWT token cho người dùng
        return jwtTokenUtil.generateToken(user);
    }

    public User getUserDetailsFromToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new RuntimeException("Token is expired");
        }
        String subject = jwtTokenUtil.extractEmail(token);
        Optional<User> user;
        user = userRepository.findByEmail(subject);
        return user.orElseThrow(() -> new Exception("User not found"));
    }
}
