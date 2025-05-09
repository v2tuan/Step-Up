package com.stepup.controller;

import com.stepup.dtos.requests.AddressDTO;
import com.stepup.dtos.requests.AddressRequest;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Address;
import com.stepup.entity.User;
import com.stepup.mapper.IAddressMapper;
import com.stepup.service.impl.AddressServiceImpl;
import com.stepup.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private IAddressMapper addressMapper;

    // 🟢 1. API Tạo Địa Chỉ Mới
    @PostMapping
    public ResponseEntity<?> createAddress(
            @Valid @RequestBody AddressRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(errors.toString())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                User user = (User) principal;
                request.getAddress().setUserId(user.getId());
                Address newAddress = addressService.saveAddress(request.getAddress());
                // Gọi xử lý nếu setDefault = true
                if (request.isSetDefault()) {
                    boolean isSet = addressService.setDefaultAddress(request.getAddress().getId(),user);
                }
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .message("Created new address successfully")
                                .status(HttpStatus.OK)
                                .data(newAddress)
                                .build()
                );
            } else {
                throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Error: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }


    @GetMapping("/user/addresses")
    public ResponseEntity<Map<String, Object>> getAddressesByUserId() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                User user = (User) principal;
                Long userId = user.getId();

                // Lấy danh sách địa chỉ
                List<AddressDTO> addresses = addressService.getAddressesByUserId(userId);

                // Tạo dữ liệu phản hồi
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("addresses", addresses != null ? addresses : Collections.emptyList());
                responseData.put("defaultAddressId", user.getDefaultAddress() != null ? user.getDefaultAddress().getId() : null);

                if (addresses == null || addresses.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
                }

                return ResponseEntity.ok(responseData);
            } else {
                throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
            }
        } catch (Exception e) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("addresses", Collections.emptyList());
            errorData.put("defaultAddressId", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorData);
        }
    }

    // 🟢 3. API Lấy Địa Chỉ Theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Long id) {
        try {
            Optional<Address> address = addressService.findById(id);

            if (address.isPresent()) {
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .message("Lấy địa chỉ thành công")
                                .status(HttpStatus.OK)
                                .data(address.get())
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseObject.builder()
                                .message("Không tìm thấy địa chỉ với ID: " + id)
                                .status(HttpStatus.NOT_FOUND)
                                .data(null)
                                .build()
                );
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Đã xảy ra lỗi: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request,
            BindingResult bindingResult
    ) {
        // Kiểm tra lỗi đầu vào
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(errors.toString())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = (User) principal;
            // Cập nhật địa chỉ
            Address address = addressService.updateAddress(id, request.getAddress());
            if (request.isSetDefault()) {
                boolean isSet = addressService.setDefaultAddress(request.getAddress().getId(),user);
            }
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Cập nhật địa chỉ thành công")
                            .status(HttpStatus.OK)
                            .data(address)
                            .build()
            );
        } catch (EntityNotFoundException e) {
            // Xử lý khi không tìm thấy địa chỉ
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.NOT_FOUND)
                            .data(null)
                            .build()
            );
        } catch (Exception e) {
            // Xử lý lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Đã xảy ra lỗi: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }

    // 🟢 5. API Xóa Địa Chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            boolean isDeleted = addressService.deleteAddress(id);

            if (isDeleted) {
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .message("Xóa địa chỉ thành công")
                                .status(HttpStatus.OK)
                                .data(null)
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseObject.builder()
                                .message("Không tìm thấy địa chỉ với ID: " + id)
                                .status(HttpStatus.NOT_FOUND)
                                .data(null)
                                .build()
                );
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Đã xảy ra lỗi: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }

    @PutMapping("/set-default/{id}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long id) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                User user = (User) principal;
                boolean isSet = addressService.setDefaultAddress(id,user);
                if (isSet) {
                    return ResponseEntity.ok(
                            ResponseObject.builder()
                                    .message("Đặt địa chỉ mặc định thành công")
                                    .status(HttpStatus.OK)
                                    .data(null)
                                    .build()
                    );

                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            ResponseObject.builder()
                                    .message("Không tìm thấy địa chỉ với ID: " + id)
                                    .status(HttpStatus.NOT_FOUND)
                                    .data(null)
                                    .build()
                    );
                }
            } else {
                throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Đã xảy ra lỗi: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/get-default")
    @Transactional
    public ResponseEntity<?> getDefaultAddress() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                User user = userService.getUserByEmail(((User) principal).getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));
//                Long userId = user.getId();
                Address defaultAddress = user.getDefaultAddress();
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .message("Lấy địa chỉ mặc định thành công")
                                .status(HttpStatus.OK)
                                .data(addressMapper.toAddressResponse(defaultAddress))
                                .build()
                );
            } else {
                throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
            }
    }
}
