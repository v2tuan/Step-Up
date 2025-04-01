package com.stepup.controller;

import com.stepup.dtos.requests.AddressDTO;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Address;
import com.stepup.service.impl.AddressServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;

    // 🟢 1. API Tạo Địa Chỉ Mới
    @PostMapping
    public ResponseEntity<?> createAddress(
            @Valid @RequestBody AddressDTO address,
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
            Address newAddress = addressService.saveAddress(address);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Created new address successfully")
                            .status(HttpStatus.OK)
                            .data(newAddress)
                            .build()
            );

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
  // lấy danh sách địa chỉ theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAddressesByUserId(@PathVariable Long userId) {
        try {
            List<AddressDTO> addresses = addressService.getAddressesByUserId(userId);

            if (addresses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseObject.builder()
                                .message("Không tìm thấy địa chỉ nào cho User ID: " + userId)
                                .status(HttpStatus.NOT_FOUND)
                                .data(null)
                                .build()
                );
            }

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Lấy danh sách địa chỉ thành công")
                            .status(HttpStatus.OK)
                            .data(addresses)
                            .build()
            );

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
            @Valid @RequestBody AddressDTO updatedAddress,
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
            // Cập nhật địa chỉ
            Address address = addressService.updateAddress(id, updatedAddress);
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
}
