package com.aws.certification_practice.repository;

import com.aws.certification_practice.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    Boolean existsByCode(String code);

    boolean existsById(Long id);

}
