package com.translator.service;

import com.translator.dto.AugmentationJobResponse;
import com.translator.dto.AugmentationRequest;
import com.translator.dto.CreateTrainingExampleRequest;
import com.translator.entity.AugmentationJob;
import com.translator.entity.Language;
import com.translator.repository.AugmentationJobRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AugmentationService {

    private final LanguageService languageService;
    private final AugmentationJobRepository augmentationJobRepository;
    private final TrainingExampleService trainingExampleService;
    private final OllamaClient ollamaClient;

    public AugmentationService(
            LanguageService languageService,
            AugmentationJobRepository augmentationJobRepository,
            TrainingExampleService trainingExampleService,
            OllamaClient ollamaClient
    ) {
        this.languageService = languageService;
        this.augmentationJobRepository = augmentationJobRepository;
        this.trainingExampleService = trainingExampleService;
        this.ollamaClient = ollamaClient;
    }

    @Transactional
    public AugmentationJobResponse start(AugmentationRequest request) {
        Language sourceLanguage = languageService.getByCodeOrThrow(request.sourceLanguageCode());
        Language targetLanguage = languageService.getByCodeOrThrow(request.targetLanguageCode());

        AugmentationJob job = new AugmentationJob(
                sourceLanguage,
                targetLanguage,
                normalizeNullable(request.domain()),
                request.count()
        );

        AugmentationJob saved = augmentationJobRepository.save(job);

        runAsync(saved.getId());

        return toResponse(saved);
    }

    @Async
    @Transactional
    public void runAsync(Long jobId) {
        AugmentationJob job = augmentationJobRepository.findById(jobId)
                .orElseThrow();

        try {
            job.markRunning();
            augmentationJobRepository.save(job);

            int created = 0;

            for (int i = 0; i < job.getRequestedCount(); i++) {
                String prompt = """
                        Создай один короткий пример параллельного перевода.
                        Верни строго две строки:
                        SOURCE: текст на исходном языке
                        TARGET: перевод на целевом языке
                        """;

                String generated = ollamaClient.generate(prompt);

                String source = extractLine(generated, "SOURCE:");
                String target = extractLine(generated, "TARGET:");

                if (!source.isBlank() && !target.isBlank()) {
                    trainingExampleService.create(new CreateTrainingExampleRequest(
                            job.getSourceLanguage().getCode(),
                            job.getTargetLanguage().getCode(),
                            source,
                            target,
                            job.getDomain()
                    ));

                    created++;
                }
            }

            job.markCompleted(created);
            augmentationJobRepository.save(job);
        } catch (Exception exception) {
            job.markFailed(exception.getMessage());
            augmentationJobRepository.save(job);
        }
    }

    @Transactional(readOnly = true)
    public List<AugmentationJobResponse> findByLanguage(String languageCode) {
        Language language = languageService.getByCodeOrThrow(languageCode);

        return augmentationJobRepository
                .findBySourceLanguageOrTargetLanguageOrderByCreatedAtDesc(language, language)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private String extractLine(String text, String prefix) {
        return text.lines()
                .filter(line -> line.startsWith(prefix))
                .map(line -> line.substring(prefix.length()).trim())
                .findFirst()
                .orElse("");
    }

    private AugmentationJobResponse toResponse(AugmentationJob job) {
        return new AugmentationJobResponse(
                job.getId(),
                job.getSourceLanguage().getCode(),
                job.getTargetLanguage().getCode(),
                job.getDomain(),
                job.getStatus().name(),
                job.getRequestedCount(),
                job.getCreatedCount(),
                job.getErrorMessage(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }

    private String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim().toLowerCase();
    }
}