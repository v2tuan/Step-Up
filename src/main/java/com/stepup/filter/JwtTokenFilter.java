package com.stepup.filter;

import com.stepup.entity.User;
import com.stepup.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    // Danh sách các đường dẫn bỏ qua xác thực
    List<String> bypassPaths = Arrays.asList(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/users/login",
            "/api/v1/users/register",
            "/coupon/**",
//            "/admin/**",
            "/login",
            "/css/**",
            "/js/**",
            "/img/**",
//            "/ws/websocket",
            "/api/v1/**" // Cần cấu hình lại sau cái này chỉ để test
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        // Bỏ qua xác thực nếu request thuộc danh sách bypassPaths
        for (String path : bypassPaths) {
            if (new AntPathMatcher().match("/api/v1/cart/**", requestPath)) {
                break;
            }
            if (new AntPathMatcher().match("/api/v1/chat/**", requestPath)) {
                break;
            }
            if (new AntPathMatcher().match("/api/v1/users/profile", requestPath)) {
                break;
            }
            if (new AntPathMatcher().match("/api/v1/favorite/**", requestPath)) {
                break;
            }
            if (new AntPathMatcher().match("/api/v1/products/**", requestPath)) {
                break;
            }
            if (new AntPathMatcher().match("/api/v1/orders/**", requestPath)) {
                break;
            }
            if(new AntPathMatcher().match("/api/v1/reviews/**", requestPath)) {
                break;
            }
            if(new AntPathMatcher().match("/api/v1/search/**", requestPath)) {
                break;
            }
            if (new AntPathMatcher().match(path, requestPath)) {
                filterChain.doFilter(request, response);
                return;
            }
            if (new AntPathMatcher().match("/api/v1/address/**", requestPath)) {
                break;
            }
        }

        // Tìm token từ cả Cookie và Header
        String token = null;

        // 1. Kiểm tra token trong Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Nếu không tìm thấy trong Cookie, kiểm tra trong Header
        if (token == null) {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7); // Cắt "Bearer " để lấy token thực sự
            }
        }

        // Xử lý khi không tìm thấy token từ cả hai nguồn
        if (token == null) {
            String acceptHeader = request.getHeader("Accept");
            if (acceptHeader != null && acceptHeader.contains("text/html")) {
                response.sendRedirect("/login");
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization token");
            }
            return;
        }

        try {
            // Lấy email từ token
            String email = jwtTokenUtil.extractEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Kiểm tra xem token JWT có hợp lệ không
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                // Tạo đối tượng xác thực chứa thông tin người dùng
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, // Đối tượng UserDetails chứa thông tin người dùng
                                null, // Không cần mật khẩu vì đã xác thực bằng JWT
                                userDetails.getAuthorities() // Danh sách quyền hạn của người dùng
                        );

                // Đặt đối tượng xác thực vào SecurityContext của Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            String acceptHeader = request.getHeader("Accept");
            if (acceptHeader != null && acceptHeader.contains("text/html")) {
                response.sendRedirect("/login");
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization token");
            }
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}