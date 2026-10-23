package com.translator.repository;

import com.translator.entity.AugmentationJob;
import com.translator.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AugmentationJobRepository extends JpaRepository<AugmentationJob, Long> {

    List<AugmentationJob> findBySourceLanguageOrTargetLanguageOrderByCreatedAtDesc(
            Language sourceLanguage,
            Language targetLanguage
    );
}