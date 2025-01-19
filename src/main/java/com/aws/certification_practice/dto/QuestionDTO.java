package com.aws.certification_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long certificationId;
    private String question;
    private Long addedBy;
    private Long updatedBy;
    private Boolean isEnabled;
}
