package com.translator.repository;

import com.translator.entity.Language;
import com.translator.entity.TranslationExample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationExampleRepository extends JpaRepository<TranslationExample, Long> {

    List<TranslationExample> findTop5BySourceLanguageAndTargetLanguageOrderByCreatedAtDesc(
            Language sourceLanguage,
            Language targetLanguage
    );

    List<TranslationExample> findTop5BySourceLanguageAndTargetLanguageAndDomainOrderByCreatedAtDesc(
            Language sourceLanguage,
            Language targetLanguage,
            String domain
    );
}