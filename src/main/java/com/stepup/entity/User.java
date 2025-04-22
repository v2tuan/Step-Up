package com.stepup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stepup.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Giá trị khóa chính sẽ tự động tăng.
    private long id;

    private String fullName;
    private String givenName;
    private String familyName;

    @Column(name = "profile_image", length = 255)
    private String profileImage;
    private String slug; // Chuỗi URL-friendly (thường dùng cho SEO).
    private String email;
    private String phone;
    private boolean isEmailActive; // Xác định email có được kích hoạt hay không.
    private boolean isPhoneActive; // Xác định số điện thoại có được kích hoạt hay không.
    private String password;

    @Column(name = "google_account_id")
    private String googleAccountId;

    @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng chuỗi trong cơ sở dữ liệu.
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user") // Dùng để lưu danh sách các giá trị đơn giản trong một bảng phụ.
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;

    // Thêm trường để lưu địa chỉ mặc định
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_address_id")
    @JsonIgnore
    private Address defaultAddress;    private int points;

    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    // Khi gọi authenticationManager.authenticate(authenticationToken);, Spring Security sẽ tự động kiểm tra isEnabled(), nếu false, nó sẽ ném ra ngoại lệ DisabledException.
    private boolean enabled;

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

