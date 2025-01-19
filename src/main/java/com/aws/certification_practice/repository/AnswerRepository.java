package com.aws.certification_practice.repository;

import com.aws.certification_practice.entity.Answer;
import com.aws.certification_practice.entity.OptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, OptionKey> {
}
