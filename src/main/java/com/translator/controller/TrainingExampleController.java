package com.translator.controller;

import com.translator.dto.CreateTrainingExampleRequest;
import com.translator.dto.TranslationExampleResponse;
import com.translator.service.TrainingExampleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/training/examples")
public class TrainingExampleController {

    private final TrainingExampleService trainingExampleService;

    public TrainingExampleController(TrainingExampleService trainingExampleService) {
        this.trainingExampleService = trainingExampleService;
    }

    @PostMapping
    public TranslationExampleResponse create(@Valid @RequestBody CreateTrainingExampleRequest request) {
        return trainingExampleService.create(request);
    }
}