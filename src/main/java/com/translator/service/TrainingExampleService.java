package com.translator.service;

import com.translator.dto.CreateTrainingExampleRequest;
import com.translator.dto.TranslationExampleResponse;
import com.translator.entity.Language;
import com.translator.entity.TranslationExample;
import com.translator.repository.TranslationExampleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingExampleService {

    private final LanguageService languageService;
    private final TranslationExampleRepository translationExampleRepository;

    public TrainingExampleService(
            LanguageService languageService,
            TranslationExampleRepository translationExampleRepository
    ) {
        this.languageService = languageService;
        this.translationExampleRepository = translationExampleRepository;
    }

    @Transactional
    public TranslationExampleResponse create(CreateTrainingExampleRequest request) {
        Language sourceLanguage = languageService.getByCodeOrThrow(request.sourceLanguageCode());
        Language targetLanguage = languageService.getByCodeOrThrow(request.targetLanguageCode());

        TranslationExample example = new TranslationExample(
                sourceLanguage,
                targetLanguage,
                request.sourceText().trim(),
                request.targetText().trim(),
                normalizeNullable(request.domain())
        );

        TranslationExample saved = translationExampleRepository.save(example);

        return toResponse(saved);
    }

    private TranslationExampleResponse toResponse(TranslationExample example) {
        return new TranslationExampleResponse(
                example.getId(),
                example.getSourceLanguage().getCode(),
                example.getTargetLanguage().getCode(),
                example.getSourceText(),
                example.getTargetText(),
                example.getDomain(),
                example.getCreatedAt()
        );
    }

    private String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim().toLowerCase();
    }
}