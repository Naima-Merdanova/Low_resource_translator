package com.translator.dto;

import java.time.OffsetDateTime;

public record TranslationExampleResponse(
        Long id,
        String sourceLanguageCode,
        String targetLanguageCode,
        String sourceText,
        String targetText,
        String domain,
        OffsetDateTime createdAt
) {
}