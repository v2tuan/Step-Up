package com.stepup.service;
import com.stepup.entity.User;

import java.util.List;
import java.util.Optional;
public interface IUserService {

    // Lấy danh sách tất cả người dùng
    List<User> getAllUsers();

    //  Lấy thông tin người dùng theo ID
    Optional<User> getUserById(Long userId);

    //  Tìm kiếm người dùng theo email
    Optional<User> getUserByEmail(String email);

    //  Tìm kiếm người dùng theo số điện thoại
    Optional<User> getUserByPhone(String phone);

    //  Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    //  Kiểm tra số điện thoại đã tồn tại chưa
    boolean existsByPhone(String phone);

    //  Lưu hoặc cập nhật thông tin người dùng
    User saveUser(User user);

    //  Xóa người dùng theo ID
    void deleteUser(Long userId);

    //  Lấy danh sách người dùng đã kích hoạt email
    List<User> getUsersByEmailActive(boolean isEmailActive);

    //  Lấy danh sách người dùng đã kích hoạt số điện thoại
    List<User> getUsersByPhoneActive(boolean isPhoneActive);
    // Lấy danh sách người dùng theo vai trò
    List<User> getUsersByRole(String role);
}
