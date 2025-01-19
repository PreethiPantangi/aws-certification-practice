package com.aws.certification_practice.controller;

import com.aws.certification_practice.dto.CertificationDTO;
import com.aws.certification_practice.service.CertificationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/certification")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addCertification(@RequestBody CertificationDTO certificationDTO) throws IOException {
        return certificationService.addCertification(certificationDTO);
    }

    @GetMapping
    public ResponseEntity<?> getCertifications(HttpServletResponse response) throws IOException{
        return certificationService.getCertifications(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id) throws IOException{
        return certificationService.deleteCertification(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertification(@PathVariable Long id, @RequestBody CertificationDTO certificationDTO) throws IOException {
        return certificationService.updateCertification(id, certificationDTO);
    }

}
