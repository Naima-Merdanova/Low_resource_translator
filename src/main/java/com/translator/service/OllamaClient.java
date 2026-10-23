package com.translator.service;

import com.translator.config.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class OllamaClient {

    private final RestClient restClient;
    private final AppProperties appProperties;

    public OllamaClient(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.restClient = RestClient.builder()
                .baseUrl(appProperties.ollama().baseUrl())
                .build();
    }

    public String generate(String prompt) {
        Map<String, Object> request = Map.of(
                "model", appProperties.ollama().model(),
                "prompt", prompt,
                "stream", false
        );

        OllamaGenerateResponse response = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(OllamaGenerateResponse.class);

        if (response == null || response.response() == null) {
            return "";
        }

        return response.response().trim();
    }

    private record OllamaGenerateResponse(
            String response
    ) {
    }
}