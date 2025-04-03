package com.stepup.controller;

import ch.qos.logback.core.subst.Token;
import com.stepup.dtos.requests.UserDTO;
import com.stepup.dtos.requests.UserLoginDTO;
import com.stepup.dtos.requests.VerifyAccountDTO;
import com.stepup.dtos.responses.LoginResponse;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.User;
import com.stepup.service.impl.AuthService;
import com.stepup.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    //can we register an "admin" user ?
    public ResponseEntity<ResponseObject> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(errorMessages.toString())
                    .build());
        }

        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            //registerResponse.setMessage();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message("Mật khẩu không khớp")
                    .build());
        }
        User user = userService.createUser(userDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .data(user)
                .message("Account registration successful")
                .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyAccountDTO verifyAccountDTO
                        , BindingResult result) {
        System.out.println("Received verification request for email: " + verifyAccountDTO.getEmail());
        System.out.println("OTP provided: " + verifyAccountDTO.getVerificationCode());
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(errorMessages.toString())
                    .build());
        }
        userService.verifyUser(verifyAccountDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .message("Account verified successfully")
                .build());
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseObject> resendOtp(@RequestParam("email") String email) {
        try {
            userService.resendVerificationCode(email);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.OK)
                            .data(null)
                            .message("Verification code has been resent successfully")
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseObject.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult result){
        String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }

    //Android, bấm đăng nhập gg, redirect đến trang đăng nhập google, đăng nhập xong có "code"
    //Từ "code" => google token => lấy ra các thông tin khác
    @GetMapping("/auth/social-login")
    public ResponseEntity<String> socialAuth(
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ){
        //request.getRequestURI()
        loginType = loginType.trim().toLowerCase();  // Loại bỏ dấu cách và chuyển thành chữ thường
        String url = authService.generateAuthUrl();
        return ResponseEntity.ok(url);
    }

    @PostMapping("/auth/social/callback")
    public String callback(
            @RequestParam("code") String code,
            HttpServletRequest request
    ) throws Exception {
        // Call the AuthService to get user info
        Map<String, Object> userInfo = authService.authenticateAndFetchProfile(code);

//        if (userInfo == null) {
//            return ResponseEntity.badRequest().body(new ResponseObject(
//                    "Failed to authenticate", HttpStatus.BAD_REQUEST, null
//            ));
//        }

        if (userInfo == null) {
            return
                    "Failed to authenticate";
        }

        // Extract user information from userInfo map

        String accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
        String name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
        String givenName = (String) Objects.requireNonNullElse(userInfo.get("given_name"), "");
        String familyName = (String) Objects.requireNonNullElse(userInfo.get("family_name"), "");
        String picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");
        String email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");

        // Tạo đối tượng UserLoginDTO
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .email(email)
                .fullname(name)
                .givenName(givenName)
                .familyName(familyName)
                .password("")
                .phoneNumber("")
                .profileImage(picture)
                .build();


        userLoginDTO.setGoogleAccountId(accountId);

//        return this.loginSocial(userLoginDTO, request).getBody().getData();
        return loginSocial(userLoginDTO, request);
    }

    private String loginSocial(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ) throws Exception {
        // Gọi hàm loginSocial từ UserService cho đăng nhập mạng xã hội
        String token = userService.loginSocial(userLoginDTO);

        // Xử lý token và thông tin người dùng
        String userAgent = request.getHeader("User-Agent");
        User userDetail = userService.getUserDetailsFromToken(token);
//        Token jwtToken = tokenService.addToken(userDetail, token, isMobileDevice(userAgent));
//
//        // Tạo đối tượng LoginResponse
//        LoginResponse loginResponse = LoginResponse.builder()
//                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
//                .token(jwtToken.getToken())
//                .tokenType(jwtToken.getTokenType())
//                .refreshToken(jwtToken.getRefreshToken())
//                .username(userDetail.getUsername())
//                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
//                .id(userDetail.getId())
//                .build();

        // Trả về phản hồi
//        return ResponseEntity.ok().body(
//                ResponseObject.builder()
//                        .message("Login successfully")
//                        .data(token)
//                        .status(HttpStatus.OK)
//                        .build()
//        );
        return token;
    }

    private boolean isMobileDevice(String userAgent) {
        // Kiểm tra User-Agent header để xác định thiết bị di động
        // Ví dụ đơn giản:
        return userAgent.toLowerCase().contains("mobile");
    }

    @GetMapping("/check-token")
    public ResponseEntity<String> checkToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        if (userService.checkToken(token)) {
            return ResponseEntity.ok("Valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
}
