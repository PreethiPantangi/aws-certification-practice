package com.aws.certification_practice.service;

import com.aws.certification_practice.dto.OptionDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface OptionService {

    ResponseEntity<?> addOption(List<OptionDTO> optionDTOs) throws IOException;

    ResponseEntity<?> getOptions() throws IOException;

    ResponseEntity<?> updateOption(Long id, OptionDTO optionDTO) throws IOException;

    ResponseEntity<?> deleteOption(Long id) throws IOException;

}
