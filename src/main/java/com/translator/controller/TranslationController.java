package com.translator.controller;

import com.translator.dto.TranslateRequest;
import com.translator.dto.TranslateResponse;
import com.translator.service.TranslationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/translate")
public class TranslationController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public TranslateResponse translate(@Valid @RequestBody TranslateRequest request) {
        return translationService.translate(request);
    }
}