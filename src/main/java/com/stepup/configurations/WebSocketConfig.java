package com.stepup.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Tiền tố cho các endpoint gửi tin nhắn đến client
        registry.enableSimpleBroker("/topic", "/queue");

        // Tiền tố cho các endpoint nhận tin nhắn từ client
        registry.setApplicationDestinationPrefixes("/app");
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // Endpoint để client kết nối WebSocket
//        registry.addEndpoint("/ws")
//                // Cho phép CORS để Android app có thể kết nối
//                .setAllowedOriginPatterns("*");
//                // Hỗ trợ SockJS fallback nếu WebSocket không được hỗ trợ

    /// /                .withSockJS();
//    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").withSockJS();
//    }

    // Định nghĩa endpoint để client có thể kết nối WebSocket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Cho client web (JS) dùng SockJS
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Cho Android dùng raw WebSocket (không dùng SockJS)
        registry.addEndpoint("/ws/websocket")
                .setAllowedOriginPatterns("*"); // Quan trọng để tránh lỗi CORS
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Đăng ký interceptor để xác thực người dùng khi kết nối WebSocket
        registration.interceptors(webSocketAuthInterceptor);
    }
}
