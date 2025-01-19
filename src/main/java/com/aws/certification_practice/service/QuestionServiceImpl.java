package com.aws.certification_practice.service;

import com.aws.certification_practice.config.JWTTokenProvider;
import com.aws.certification_practice.dto.QuestionDTO;
import com.aws.certification_practice.dto.QuestionResponseDTO;
import com.aws.certification_practice.entity.Certification;
import com.aws.certification_practice.entity.Question;
import com.aws.certification_practice.entity.User;
import com.aws.certification_practice.repository.CertificationRepository;
import com.aws.certification_practice.repository.QuestionRepository;
import com.aws.certification_practice.repository.UserRepository;
import com.aws.certification_practice.utils.ResponseUtils;
import com.aws.certification_practice.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    UserUtils userUtils;

    @Autowired
    ResponseUtils responseUtils;

    @Override
    public ResponseEntity<?> getQuestions(HttpServletResponse response) throws IOException {
        List<Question> questionsList = questionRepository.findAll();
        List<QuestionResponseDTO> questions = questionsList
                .stream()
                .map(question -> new QuestionResponseDTO(
                        question.getCertification().getId(),
                        question.getQuestion(),
                        question.getAddedAt(),
                        question.getUpdatedAt(),
                        question.getAddedBy().getId(),
                        question.getUpdatedBy().getId(),
                        question.getIsEnabled())
                )
                .collect(Collectors.toList());
        return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Success", questions);
    }

    @Override
    public ResponseEntity<?> getQuestionsByCertification(Long certificationId) throws IOException {
        List<Question> questionsList = questionRepository.findAllByCertification_Id(certificationId);
        List<QuestionResponseDTO> questions = questionsList
                .stream()
                .map(question -> new QuestionResponseDTO(
                        question.getCertification().getId(),
                        question.getQuestion(),
                        question.getAddedAt(),
                        question.getUpdatedAt(),
                        question.getAddedBy().getId(),
                        question.getUpdatedBy().getId(),
                        question.getIsEnabled())
                )
                .collect(Collectors.toList());
        return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Success", questions);
    }

    @Override
    public ResponseEntity<?> addQuestion(List<QuestionDTO> questionDTOs) throws IOException {

        int totalQuestions = questionDTOs.size();
        int successfulQuestions = 0;
        int failedQuestions = 0;
        List<Map<String, Object>> responses = new ArrayList<>();

        for(QuestionDTO questionDTO : questionDTOs) {
            Map<String, Object> questionResponse = new HashMap<>();
            if(questionRepository.existsByQuestion(questionDTO.getQuestion())) {
                questionResponse.put("status", HttpServletResponse.SC_CONFLICT);
                questionResponse.put("message", "Question already exists.");
                Map<String, Object> questionData = new HashMap<>();
                questionData.put("question", questionDTO.getQuestion());
                questionResponse.put("data", questionData);
                failedQuestions += 1;
            } else {
                User user = userUtils.getUser();
                Certification certification = certificationRepository.findById(questionDTO.getCertificationId()).orElseThrow(() -> new IllegalArgumentException("Certification with id " + questionDTO.getCertificationId() + " does not exists"));
                Question question = Question.builder()
                        .certification(certification)
                        .question(questionDTO.getQuestion())
                        .addedBy(user)
                        .updatedBy(user)
                        .isEnabled(true)
                        .build();
                Map<String, Object> data = new HashMap<>();
                try {
                    Question savedQuestion = questionRepository.save(question);
                    if(savedQuestion.getId() != null) {
                        questionResponse.put("status", HttpServletResponse.SC_OK);
                        questionResponse.put("message", "Added question successfully!");
                        data.put("id", question.getId());
                        data.put("question", question.getQuestion());
                        data.put("certificationId", question.getCertification().getId());
                        data.put("addedAt", question.getAddedAt());
                        data.put("addedBy", question.getAddedBy().getId());
                        questionResponse.put("data", data);
                        successfulQuestions += 1;
                    } else {
                        questionResponse.put("status", HttpServletResponse.SC_OK);
                        questionResponse.put("message", "Save operation failed. No exception was thrown, but entity is null.");
                        Map<String, Object> questionData = new HashMap<>();
                        questionData.put("question", questionDTO.getQuestion());
                        questionResponse.put("data", questionData);
                    }
                } catch(Exception e) {
                    questionResponse.put("status", HttpServletResponse.SC_NOT_FOUND);
                    questionResponse.put("message", e.getMessage());
                    Map<String, Object> questionData = new HashMap<>();
                    questionData.put("question", questionDTO.getQuestion());
                    questionResponse.put("data", questionData);
                }

            }
            responses.add(questionResponse);
        }

        if(failedQuestions == totalQuestions) {
            return responseUtils.buildResponse(HttpServletResponse.SC_CONFLICT, "Failed to add all the questions");
        } else if (successfulQuestions == totalQuestions) {
            return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Successfully added all questions");
        } else {
            return responseUtils.buildResponse(HttpServletResponse.SC_MULTIPLE_CHOICES, "Partial success");
        }

    }

    @Override
    public ResponseEntity<?> updateQuestion(QuestionDTO questionDTO, Long id) throws IOException {
        Optional<Question> existingQuestionOpt = questionRepository.findById(id);
        if(existingQuestionOpt.isEmpty()) {
            return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "Question does not exist!");
        }
        Question existingQuestion = existingQuestionOpt.get();
        if(questionDTO.getQuestion() != null) {
            existingQuestion.setQuestion(questionDTO.getQuestion());
        }

        if(questionDTO.getCertificationId() != null) {
            Certification certification = certificationRepository.findById(questionDTO.getCertificationId()).orElseThrow(() -> new IllegalArgumentException("Certification does not exist!"));
            existingQuestion.setCertification(certification);
        }

        if(questionDTO.getIsEnabled() != null) {
            existingQuestion.setIsEnabled(questionDTO.getIsEnabled());
        }

        existingQuestion.setUpdatedBy(userUtils.getUser());

        questionRepository.save(existingQuestion);

        Map<String, Object> data = new HashMap<>();
        data.put("id", existingQuestion.getId());
        data.put("question", existingQuestion.getQuestion());
        data.put("certificationId", existingQuestion.getCertification().getId());
        data.put("updatedAt", existingQuestion.getUpdatedAt());
        data.put("updatedBy", existingQuestion.getUpdatedBy().getId());

        return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Updated question details successfully!", data);
    }

    @Override
    public ResponseEntity<?> deleteQuestion(Long id) throws IOException {
        if(!questionRepository.existsById(id)) {
            return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "Question does not exist!");
        }
        questionRepository.deleteById(id);
        return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Deleted question successfully!");
    }

}
