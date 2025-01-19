package com.aws.certification_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponseDTO {
    private Long id;
    private String option;
    private Long addedBy;
    private LocalDateTime addedAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
