package com.stepup.components;
import com.stepup.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
    }
}
