package com.translator.service;

import com.translator.dto.TranslateRequest;
import com.translator.dto.TranslateResponse;
import com.translator.entity.Language;
import com.translator.entity.TranslationExample;
import com.translator.repository.TranslationExampleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TranslationService {

    private final LanguageService languageService;
    private final TranslationExampleRepository translationExampleRepository;
    private final OllamaClient ollamaClient;

    public TranslationService(
            LanguageService languageService,
            TranslationExampleRepository translationExampleRepository,
            OllamaClient ollamaClient
    ) {
        this.languageService = languageService;
        this.translationExampleRepository = translationExampleRepository;
        this.ollamaClient = ollamaClient;
    }

    @Transactional(readOnly = true)
    public TranslateResponse translate(TranslateRequest request) {
        Language sourceLanguage = languageService.getByCodeOrThrow(request.sourceLanguageCode());
        Language targetLanguage = languageService.getByCodeOrThrow(request.targetLanguageCode());

        List<TranslationExample> examples = findExamples(
                sourceLanguage,
                targetLanguage,
                request.domain()
        );

        String prompt = buildPrompt(sourceLanguage, targetLanguage, request.text(), examples);
        String translatedText = ollamaClient.generate(prompt);

        return new TranslateResponse(
                sourceLanguage.getCode(),
                targetLanguage.getCode(),
                request.text(),
                translatedText
        );
    }

    private List<TranslationExample> findExamples(
            Language sourceLanguage,
            Language targetLanguage,
            String domain
    ) {
        if (domain != null && !domain.isBlank()) {
            return translationExampleRepository
                    .findTop5BySourceLanguageAndTargetLanguageAndDomainOrderByCreatedAtDesc(
                            sourceLanguage,
                            targetLanguage,
                            domain.trim().toLowerCase()
                    );
        }

        return translationExampleRepository
                .findTop5BySourceLanguageAndTargetLanguageOrderByCreatedAtDesc(
                        sourceLanguage,
                        targetLanguage
                );
    }

    private String buildPrompt(
            Language sourceLanguage,
            Language targetLanguage,
            String text,
            List<TranslationExample> examples
    ) {
        StringBuilder builder = new StringBuilder();

        builder.append("Ты профессиональный переводчик.\n");
        builder.append("Переведи текст с языка ")
                .append(sourceLanguage.getName())
                .append(" на язык ")
                .append(targetLanguage.getName())
                .append(".\n");
        builder.append("Верни только перевод без объяснений.\n\n");

        if (!examples.isEmpty()) {
            builder.append("Примеры переводов:\n");

            for (TranslationExample example : examples) {
                builder.append("Исходный текст: ")
                        .append(example.getSourceText())
                        .append("\n");

                builder.append("Перевод: ")
                        .append(example.getTargetText())
                        .append("\n\n");
            }
        }

        builder.append("Текст для перевода:\n");
        builder.append(text);

        return builder.toString();
    }
}