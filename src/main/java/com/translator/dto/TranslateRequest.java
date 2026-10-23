package com.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TranslateRequest(
        @NotBlank
        String sourceLanguageCode,

        @NotBlank
        String targetLanguageCode,

        @NotBlank
        @Size(max = 5000)
        String text,

        @Size(max = 100)
        String domain
) {
}
