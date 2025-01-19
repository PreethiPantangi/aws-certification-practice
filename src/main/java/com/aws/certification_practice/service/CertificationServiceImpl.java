package com.aws.certification_practice.service;

import com.aws.certification_practice.config.JWTTokenProvider;
import com.aws.certification_practice.dto.CertificationDTO;
import com.aws.certification_practice.dto.CertificationResponseDTO;
import com.aws.certification_practice.entity.Certification;
import com.aws.certification_practice.entity.User;
import com.aws.certification_practice.repository.CertificationRepository;
import com.aws.certification_practice.repository.UserRepository;
import com.aws.certification_practice.utils.ResponseUtils;
import com.aws.certification_practice.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificationServiceImpl implements CertificationService{

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserUtils userUtils;

    @Autowired
    ResponseUtils responseUtils;

    @Override
    public ResponseEntity<?> addCertification(CertificationDTO certificationDTO) throws IOException {
        if(certificationRepository.existsByCode(certificationDTO.getCode())) {
            return responseUtils.buildResponse(HttpServletResponse.SC_CONFLICT, "Certification with code " + certificationDTO.getCode() + " already exists!");
        } else {
            User user = userUtils.getUser();
            Certification certification = Certification.builder()
                    .name(certificationDTO.getName())
                    .code(certificationDTO.getCode())
                    .isEnabled(true)
                    .addedBy(user)
                    .updatedBy(user)
                    .build();
            Certification savedCertification = certificationRepository.save(certification);
            Map<String, Object> certificationDetails = new HashMap<>();
            certificationDetails.put("id", savedCertification.getId());
            certificationDetails.put("name", savedCertification.getName());
            certificationDetails.put("code", savedCertification.getCode());
            certificationDetails.put("addedBy", certification.getAddedBy().getId());
            certificationDetails.put("createdAt", savedCertification.getCreatedAt());
            certificationDetails.put("isEnabled", certification.getIsEnabled());
            return responseUtils.buildResponse(HttpServletResponse.SC_CREATED, "Successfully added certification with code " + certification.getCode(), certificationDetails);
        }
    }

    @Override
    public ResponseEntity<?> getCertifications(HttpServletResponse response) throws IOException {
        List<Certification> certifications = certificationRepository.findAll();
        List<CertificationResponseDTO> allCertifications = certifications
                .stream()
                .map(certification -> new CertificationResponseDTO(certification.getId(), certification.getName(), certification.getCode(), certification.getAddedBy().getId(), certification.getUpdatedBy().getId(), certification.getCreatedAt(), certification.getUpdatedAt(), certification.getIsEnabled()))
                .collect(Collectors.toList());
        return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Success", allCertifications);
    }

    @Override
    public ResponseEntity<?> updateCertification(Long id, CertificationDTO certificationDTO) throws IOException {
        Optional<Certification> existingCertificationOpt = certificationRepository.findById(id);
        if(existingCertificationOpt.isEmpty()) {
            return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "Certification does not exists!");
        } else {
            Certification existingCertification = existingCertificationOpt.get();
            User user = userUtils.getUser();
            if(certificationDTO.getCode() != null) {
                existingCertification.setCode(certificationDTO.getCode());
            }
            if(certificationDTO.getName() != null) {
                existingCertification.setName(certificationDTO.getName());
            }
            if(certificationDTO.getIsEnabled() != null) {
                existingCertification.setIsEnabled(certificationDTO.getIsEnabled());
            }
            existingCertification.setUpdatedBy(user);
            Certification savedCertification = certificationRepository.save(existingCertification);
            Map<String, Object> certificationDetails = new HashMap<>();
            certificationDetails.put("id", savedCertification.getId());
            certificationDetails.put("name", savedCertification.getName());
            certificationDetails.put("code", savedCertification.getCode());
            certificationDetails.put("updatedBy", savedCertification.getUpdatedBy().getId());
            certificationDetails.put("updatedAt", savedCertification.getUpdatedAt());
            certificationDetails.put("isEnabled", savedCertification.getIsEnabled());
            return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Updated successfully!", certificationDetails);
        }
    }

    @Override
    public ResponseEntity<?> deleteCertification(Long id) throws IOException {
        if(!certificationRepository.existsById(id)) {
            return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "Certification does not exists!");
        } else {
            certificationRepository.deleteById(id);
            return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Deleted successfully!");
        }
    }
}
