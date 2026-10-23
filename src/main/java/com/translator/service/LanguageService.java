package com.translator.service;

import com.translator.dto.CreateLanguageRequest;
import com.translator.dto.LanguageResponse;
import com.translator.entity.Language;
import com.translator.exception.BadRequestException;
import com.translator.exception.NotFoundException;
import com.translator.repository.LanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional
    public LanguageResponse create(CreateLanguageRequest request) {
        String code = normalizeCode(request.code());

        if (languageRepository.existsByCode(code)) {
            throw new BadRequestException("Язык с кодом " + code + " уже существует");
        }

        Language language = new Language(code, request.name().trim());
        Language saved = languageRepository.save(language);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LanguageResponse> findAll() {
        return languageRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Language getByCodeOrThrow(String code) {
        return languageRepository.findByCode(normalizeCode(code))
                .orElseThrow(() -> new NotFoundException("Язык с кодом " + code + " не найден"));
    }

    private LanguageResponse toResponse(Language language) {
        return new LanguageResponse(
                language.getId(),
                language.getCode(),
                language.getName()
        );
    }

    private String normalizeCode(String code) {
        return code.trim().toLowerCase();
    }
}