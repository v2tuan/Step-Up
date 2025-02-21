package com.stepup.entity;

import com.stepup.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Giá trị khóa chính sẽ tự động tăng.
    private long _id;

    private String firstName;
    private String lastName;
    private String slug; // Chuỗi URL-friendly (thường dùng cho SEO).
    private String email;
    private String phone;
    private boolean isEmailActive; // Xác định email có được kích hoạt hay không.
    private boolean isPhoneActive; // Xác định số điện thoại có được kích hoạt hay không.
    private String password;

    @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng chuỗi trong cơ sở dữ liệu.
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user") // Dùng để lưu danh sách các giá trị đơn giản trong một bảng phụ.
    private List<Address> addresses;

    @Lob // Lưu trữ dữ liệu lớn, thường là hình ảnh hoặc file.
    private byte[] avatar;

    private int points;

    @CreationTimestamp // Tự động gán thời gian tạo bản ghi.
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật thời gian khi bản ghi bị sửa đổi.
    private LocalDateTime updatedAt;


    // Hàm callback, tự động được gọi trước khi lưu một bản ghi mới vào cơ sở dữ liệu.
    @PrePersist
    protected void onCreate() {
        if (role == null) {
            role = Role.CUSTOMER;
        }
        points = 0;
    }
}

