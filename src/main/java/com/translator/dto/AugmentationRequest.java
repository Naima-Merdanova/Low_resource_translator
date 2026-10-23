package com.translator.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AugmentationRequest(
        @NotBlank
        String sourceLanguageCode,

        @NotBlank
        String targetLanguageCode,

        String domain,

        @Min(1)
        @Max(100)
        int count
) {
}