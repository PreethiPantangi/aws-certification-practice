package com.aws.certification_practice.service;

import com.aws.certification_practice.dto.OptionDTO;
import com.aws.certification_practice.dto.OptionResponseDTO;
import com.aws.certification_practice.entity.Option;
import com.aws.certification_practice.entity.User;
import com.aws.certification_practice.repository.OptionRepository;
import com.aws.certification_practice.utils.ResponseUtils;
import com.aws.certification_practice.utils.UserUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService{

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    UserUtils userUtils;

    @Autowired
    ResponseUtils responseUtils;
    

    @Override
    public ResponseEntity<?> addOption(List<OptionDTO> optionDTOs) throws IOException {
        int totalQuestions = optionDTOs.size();
        int successfulQuestions = 0;
        int failedQuestions = 0;
        List<Map<String, Object>> responses = new ArrayList<>();

        for(OptionDTO optionDTO : optionDTOs) {
            Map<String, Object> optionResponse = new HashMap<>();
            if(optionRepository.existsByOptionValue(optionDTO.getOption())) {
                optionResponse.put("status", HttpServletResponse.SC_CONFLICT);
                optionResponse.put("message", "Option already exists.");
                Map<String, Object> optionData = new HashMap<>();
                optionData.put("option", optionDTO.getOption());
                optionResponse.put("data", optionData);
                failedQuestions += 1;
            } else {
                User user = userUtils.getUser();
                Option option = Option.builder()
                        .optionValue(optionDTO.getOption())
                        .addedBy(user)
                        .updatedBy(user)
                        .build();
                Map<String, Object> data = new HashMap<>();
                try {
                    Option savedOption = optionRepository.save(option);
                    if(savedOption.getId() != null) {
                        optionResponse.put("status", HttpServletResponse.SC_OK);
                        optionResponse.put("message", "Added question successfully!");
                        data.put("id", option.getId());
                        data.put("option", option.getOptionValue());
                        data.put("addedAt", option.getAddedBy().getId());
                        optionResponse.put("data", data);
                        successfulQuestions += 1;
                    } else {
                        optionResponse.put("status", HttpServletResponse.SC_OK);
                        optionResponse.put("message", "Save operation failed. No exception was thrown, but entity is null.");
                        Map<String, Object> optionData = new HashMap<>();
                        optionData.put("option", optionDTO.getOption());
                        optionResponse.put("data", optionData);
                    }
                } catch(Exception e) {
                    optionResponse.put("status", HttpServletResponse.SC_NOT_FOUND);
                    optionResponse.put("message", e.getMessage());
                    Map<String, Object> optionData = new HashMap<>();
                    optionData.put("option", optionDTO.getOption());
                    optionResponse.put("data", optionData);
                }

            }
            responses.add(optionResponse);
        }

        if(failedQuestions == totalQuestions) {
            return responseUtils.buildResponse(HttpServletResponse.SC_CONFLICT, "Failed to add all the options", responses);
        } else if (successfulQuestions == totalQuestions) {
            return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Successfully added all options", responses);
        } else {
            return responseUtils.buildResponse(HttpServletResponse.SC_MULTIPLE_CHOICES, "Partial success", responses);
        }
    }

    @Override
    public ResponseEntity<?> getOptions() throws IOException {
        System.out.println("getOptions");
        List<Option> optionsList = optionRepository.findAll();
        List<OptionResponseDTO> options = optionsList.stream()
                .map(option -> new OptionResponseDTO(option.getId(), option.getOptionValue(), option.getAddedBy().getId(), option.getAddedAt(), option.getUpdatedBy().getId(), option.getUpdatedAt()))
                .collect(Collectors.toList());
        return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Success", options);
    }

    @Override
    public ResponseEntity<?> updateOption(Long id, OptionDTO optionDTO) throws IOException {
        Optional<Option> existingOptionOpt = optionRepository.findById(id);
        if(existingOptionOpt.isEmpty()) {
            return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "Option does not exist!");
        } else {
            Option existingOption = existingOptionOpt.get();
            User user = userUtils.getUser();
            if(optionDTO.getOption() != null) {
                existingOption.setOptionValue(optionDTO.getOption());
            }
            existingOption.setUpdatedBy(user);
            Option savedOption = optionRepository.save(existingOption);
            Map<String, Object> optionDetails = new HashMap<>();
            optionDetails.put("id", savedOption.getId());
            optionDetails.put("option", savedOption.getOptionValue());
            optionDetails.put("Updated By", savedOption.getUpdatedBy().getId());
            optionDetails.put("Updated At", savedOption.getUpdatedAt());
            return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Updated successfully!", optionDetails);
        }
    }

    @Override
    public ResponseEntity<?> deleteOption(Long id) throws IOException {
        Optional<Option> option = optionRepository.findById(id);
        if(option.isEmpty()) {
            return responseUtils.buildResponse(HttpServletResponse.SC_NOT_FOUND, "Option does not exist!");
        } else {
            optionRepository.deleteById(id);
            return responseUtils.buildResponse(HttpServletResponse.SC_OK, "Deleted option successfully!");
        }
    }

}
