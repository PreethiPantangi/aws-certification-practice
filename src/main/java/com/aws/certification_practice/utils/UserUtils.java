package com.aws.certification_practice.utils;

import com.aws.certification_practice.config.JWTTokenProvider;
import com.aws.certification_practice.entity.User;
import com.aws.certification_practice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUtils {

    @Autowired
    HttpServletRequest request;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    public User getUser() {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String username = jwtTokenProvider.getUsername(token);
        return userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
