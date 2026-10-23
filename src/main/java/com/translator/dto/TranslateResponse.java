package com.translator.dto;

public record TranslateResponse(
        String sourceLanguageCode,
        String targetLanguageCode,
        String sourceText,
        String translatedText
) {
}