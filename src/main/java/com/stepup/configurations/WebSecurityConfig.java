package com.stepup.configurations;

import com.stepup.Enum.Role;
import com.stepup.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableWebMvc
public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers(
//                                "/assets/**", "/cdn.jsdelivr.net/**", "/cdnjs.cloudflare.com/**",
//                                "/register", "/login/**", "/api/**",
//                                "/home/**", "/forgotpassword/**", "/vnpay-payment", "/error/**"
//                        ).permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}