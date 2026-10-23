package com.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTrainingExampleRequest(
        @NotBlank
        String sourceLanguageCode,

        @NotBlank
        String targetLanguageCode,

        @NotBlank
        @Size(max = 5000)
        String sourceText,

        @NotBlank
        @Size(max = 5000)
        String targetText,

        @Size(max = 100)
        String domain
) {
}