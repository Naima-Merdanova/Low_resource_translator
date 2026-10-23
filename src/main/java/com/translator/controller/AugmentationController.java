package com.translator.controller;

import com.translator.dto.AugmentationJobResponse;
import com.translator.dto.AugmentationRequest;
import com.translator.service.AugmentationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training")
public class AugmentationController {

    private final AugmentationService augmentationService;

    public AugmentationController(AugmentationService augmentationService) {
        this.augmentationService = augmentationService;
    }

    @PostMapping("/augment")
    public AugmentationJobResponse start(@Valid @RequestBody AugmentationRequest request) {
        return augmentationService.start(request);
    }

    @GetMapping("/jobs/{languageCode}")
    public List<AugmentationJobResponse> findByLanguage(@PathVariable String languageCode) {
        return augmentationService.findByLanguage(languageCode);
    }
}