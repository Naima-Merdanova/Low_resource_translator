package com.translator.dto;

import java.time.OffsetDateTime;

public record AugmentationJobResponse(
        Long id,
        String sourceLanguageCode,
        String targetLanguageCode,
        String domain,
        String status,
        int requestedCount,
        int createdCount,
        String errorMessage,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}