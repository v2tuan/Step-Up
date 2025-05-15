package com.stepup.configurations;

import com.stepup.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
// Xác thực người dùng trước khi cho phép kết nối WebSocket.
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Lấy token từ header
            List<String> authorization = accessor.getNativeHeader("Authorization");

            if (authorization != null && !authorization.isEmpty()) {
                String token = authorization.get(0);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);

                    try {
                        // Lấy email từ token
                        String email = jwtTokenUtil.extractEmail(token);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        // Kiểm tra token có hợp lệ không
                        if (jwtTokenUtil.validateToken(token, userDetails)) {
                            // Đặt thông tin xác thực vào accessor
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            accessor.setUser(auth);
                        }
                    } catch (Exception e) {
                        // Token không hợp lệ, không đặt thông tin xác thực
                        throw new RuntimeException("Failed to extract token in webSocket", e);
                    }
                }
            }
        }

        return message;
    }
}
