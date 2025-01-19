package com.aws.certification_practice.service;

import com.aws.certification_practice.config.JWTTokenProvider;
import com.aws.certification_practice.constants.Role;
import com.aws.certification_practice.constants.UserStatus;
import com.aws.certification_practice.dto.UserDTO;
import com.aws.certification_practice.dto.UserResponseDTO;
import com.aws.certification_practice.entity.User;
import com.aws.certification_practice.repository.UserRepository;
import com.aws.certification_practice.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    ResponseUtils responseUtils;

    @Override
    public ResponseEntity<?> createUser(UserDTO userDTO, Boolean isAdmin) throws IOException {
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            return responseUtils.buildResponse(HttpServletResponse.SC_CONFLICT, "User with email " + userDTO.getEmail() + " already exists!");
        } else {
            User newUser = User.builder()
                    .firstname(userDTO.getFirstname())
                    .lastname(userDTO.getLastname())
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .isActive(UserStatus.ACTIVE)
                    .role(isAdmin ? Role.ROLE_ADMIN.toString() : Role.ROLE_USER.toString())
                    .build();
            userRepository.save(newUser);
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("firstname", newUser.getFirstname());
            userDetails.put("lastname", newUser.getLastname());
            userDetails.put("email", newUser.getEmail());
            return responseUtils.buildResponse(HttpServletResponse.SC_CREATED, isAdmin ? "User with role admin created successfully!" : "User created successfully!", userDetails);
        }
    }

    @Override
    public ResponseEntity<?> getUsers(HttpServletResponse response) throws IOException {
        List<User> allUsers = userRepository.findAll();
        List<UserResponseDTO> users = allUsers
                .stream()
                .map(user -> new UserResponseDTO(user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole(), user.getCreatedAt()))
                .collect(Collectors.toList());
        return responseUtils.buildResponse(HttpServletResponse.SC_OK,"Success",users);
    }

    @Override
    public ResponseEntity<?> validateUser(UserDTO userDTO) throws IOException {
        try {
            Authentication authentication = null;
            if(!userRepository.existsByEmail(userDTO.getEmail())) {
                return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "User with email " + userDTO.getEmail() + " doesn't exist, please create an account!");
            } else {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
                );

                Map<String, Object> data = new HashMap<>();
                if(authentication.isAuthenticated()) {
                    data.put("token", jwtTokenProvider.generateToken(authentication));
                    return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Logged in successfully!", data);
                } else {
                    return responseUtils.buildResponse(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials!");
                }
            }
        } catch(AuthenticationException authenticationException) {
            return responseUtils.buildResponse(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials!");
        }
    }
}
