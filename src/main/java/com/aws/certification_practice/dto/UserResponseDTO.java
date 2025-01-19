package com.aws.certification_practice.dto;

import com.aws.certification_practice.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
