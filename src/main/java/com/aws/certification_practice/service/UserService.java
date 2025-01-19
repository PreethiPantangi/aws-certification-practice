package com.aws.certification_practice.service;

import com.aws.certification_practice.dto.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserService {
    ResponseEntity<?> createUser(UserDTO userDTO, Boolean isAdmin) throws IOException;

    ResponseEntity<?> getUsers(HttpServletResponse response) throws IOException;

    ResponseEntity<?> validateUser(UserDTO userDTO) throws IOException;
}
