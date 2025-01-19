package com.aws.certification_practice.controller;

import com.aws.certification_practice.dto.UserDTO;
import com.aws.certification_practice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody UserDTO userDTO) throws IOException {
        return userService.createUser(userDTO, Boolean.TRUE);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(HttpServletResponse response) throws IOException {
        return userService.getUsers(response);
    }
}
