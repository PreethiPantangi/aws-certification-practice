package com.aws.certification_practice.repository;

import com.aws.certification_practice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Boolean existsByQuestion(String question);

    List<Question> findAllByCertification_Id(Long certificationId);

}
