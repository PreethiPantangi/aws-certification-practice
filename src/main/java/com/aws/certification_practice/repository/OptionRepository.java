package com.aws.certification_practice.repository;

import com.aws.certification_practice.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    Boolean existsByOptionValue(String option);

}
