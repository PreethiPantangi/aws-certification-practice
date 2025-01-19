package com.aws.certification_practice.controller;

import com.aws.certification_practice.dto.OptionDTO;
import com.aws.certification_practice.service.OptionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/option")
public class OptionController {

    @Autowired
    OptionService optionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addOption(@RequestBody List<OptionDTO> optionDTOS) throws IOException {
        return optionService.addOption(optionDTOS);
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping
    public ResponseEntity<?> getOptions() throws IOException {
        return optionService.getOptions();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOption(@PathVariable Long id, @RequestBody OptionDTO optionDTO) throws IOException {
        return optionService.updateOption(id, optionDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Long id) throws IOException {
        return optionService.deleteOption(id);
    }


}
