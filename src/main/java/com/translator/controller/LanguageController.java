package com.translator.controller;

import com.translator.dto.CreateLanguageRequest;
import com.translator.dto.LanguageResponse;
import com.translator.service.LanguageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @PostMapping
    public LanguageResponse create(@Valid @RequestBody CreateLanguageRequest request) {
        return languageService.create(request);
    }

    @GetMapping
    public List<LanguageResponse> findAll() {
        return languageService.findAll();
    }
}