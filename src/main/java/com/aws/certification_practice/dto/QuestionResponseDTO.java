package com.aws.certification_practice.dto;

import com.aws.certification_practice.entity.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long certificationId;
    private String question;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private Long addedBy;
    private Long updatedBy;
    private Boolean isEnabled;
}
