package com.springbootfirstproject.ecommerceapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow public access to homepage, static resources, and browsing
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/api/products/**", "/api/categories/**", "/api/auth/**").permitAll()
                // Require authentication for user-specific features
                .requestMatchers("/api/checkout/**", "/api/user/orders/**", "/api/orders/**", "/orders").authenticated()
                // Allow all other requests (for now)
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/", true)
                .successHandler((request, response, authentication) -> {
                    // Redirect to home page after successful login
                    response.sendRedirect("/");
                })
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/checkout")
            );

        return http.build();
    }
}
