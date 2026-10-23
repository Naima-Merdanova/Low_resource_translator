package com.translator.controller;

import com.translator.dto.TranslateResponse;
import com.translator.dto.CreateLanguageRequest;
import com.translator.dto.CreateTrainingExampleRequest;
import com.translator.dto.TranslateRequest;
import com.translator.service.LanguageService;
import com.translator.service.TrainingExampleService;
import com.translator.service.TranslationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final LanguageService languageService;
    private final TrainingExampleService trainingExampleService;
    private final TranslationService translationService;

    public WebController(
            LanguageService languageService,
            TrainingExampleService trainingExampleService,
            TranslationService translationService
    ) {
        this.languageService = languageService;
        this.trainingExampleService = trainingExampleService;
        this.translationService = translationService;
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/languages")
    public String languagesPage(Model model) {
        model.addAttribute("languages", languageService.findAll());
        return "languages";
    }

    @PostMapping("/languages")
    public String addLanguage(
            String code,
            String name,
            Model model
    ) {
        try {
            CreateLanguageRequest request = new CreateLanguageRequest(code, name);
            languageService.create(request);

            model.addAttribute("message", "Язык успешно добавлен");
        } catch (Exception exception) {
            model.addAttribute("error", exception.getMessage());
        }

        model.addAttribute("languages", languageService.findAll());
        return "languages";
    }

    @GetMapping("/training")
    public String trainingPage() {
        return "training";
    }

    @PostMapping("/training")
    public String addTrainingExample(
            String sourceLanguageCode,
            String targetLanguageCode,
            String sourceText,
            String targetText,
            String domain,
            Model model
    ) {
        try {
            CreateTrainingExampleRequest request = new CreateTrainingExampleRequest(
                    sourceLanguageCode,
                    targetLanguageCode,
                    sourceText,
                    targetText,
                    domain
            );

            trainingExampleService.create(request);

            model.addAttribute("message", "Обучающий пример успешно добавлен");
        } catch (Exception exception) {
            model.addAttribute("error", exception.getMessage());
        }

        return "training";
    }

    @GetMapping("/translate")
    public String translatePage() {
        return "translate";
    }

    @PostMapping("/translate")
    public String translate(
            String sourceLanguageCode,
            String targetLanguageCode,
            String text,
            String domain, // ВАЖНО: Проверьте наличие этой строчки здесь!
            Model model
    ) {
        try {
            TranslateRequest request = new TranslateRequest(
                    sourceLanguageCode,
                    targetLanguageCode,
                    text,
                    domain
            );

            TranslateResponse response = translationService.translate(request);

            model.addAttribute("translatedText", response.translatedText());
        } catch (Exception exception) {
            model.addAttribute("error", exception.getMessage());
        }

        return "translate";

    }
}