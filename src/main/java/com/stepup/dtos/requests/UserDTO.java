package com.stepup.dtos.requests;

import com.stepup.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng chuỗi trong cơ sở dữ liệu.
    private Role role;
}
