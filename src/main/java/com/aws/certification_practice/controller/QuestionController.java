package com.aws.certification_practice.controller;

import com.aws.certification_practice.dto.QuestionDTO;
import com.aws.certification_practice.service.QuestionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addQuestion(@RequestBody List<QuestionDTO> questionDTO) throws IOException {
        return questionService.addQuestion(questionDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getQuestions(HttpServletResponse response) throws IOException {
        return questionService.getQuestions(response);
    }

    @GetMapping("/{certificationId}")
    public ResponseEntity<?> getQuestionsByCertification(@PathVariable Long certificationId) throws IOException {
        return questionService.getQuestionsByCertification(certificationId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable Long id) throws IOException {
        return questionService.updateQuestion(questionDTO, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) throws IOException {
        System.out.println("delete question controller");
        return questionService.deleteQuestion(id);
    }

}
