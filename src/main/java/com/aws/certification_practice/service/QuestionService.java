package com.aws.certification_practice.service;

import com.aws.certification_practice.dto.QuestionDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface QuestionService {
    ResponseEntity<?> getQuestions(HttpServletResponse response) throws IOException;

    ResponseEntity<?> getQuestionsByCertification(Long certificationId) throws IOException;

    ResponseEntity<?> addQuestion(List<QuestionDTO> questionDTO) throws IOException;

    ResponseEntity<?> updateQuestion(QuestionDTO questionDTO, Long id) throws IOException;

    ResponseEntity<?> deleteQuestion(Long id) throws IOException;


}
