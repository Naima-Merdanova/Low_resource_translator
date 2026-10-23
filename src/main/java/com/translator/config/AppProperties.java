package com.translator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Ollama ollama,
        Chroma chroma
) {
    public record Ollama(
            String baseUrl,
            String model
    ) {
    }

    public record Chroma(
            String baseUrl
    ) {
    }
}