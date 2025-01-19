package com.aws.certification_practice.service;

import com.aws.certification_practice.dto.CertificationDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface CertificationService {
    ResponseEntity<?> getCertifications(HttpServletResponse response) throws IOException;
    ResponseEntity<?> addCertification(CertificationDTO certificationDTO) throws IOException;
    ResponseEntity<?> updateCertification(Long id, CertificationDTO certificationDTO) throws IOException;
    ResponseEntity<?> deleteCertification(Long id) throws IOException;


}
