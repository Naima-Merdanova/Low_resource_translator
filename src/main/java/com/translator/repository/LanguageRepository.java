package com.translator.repository;

import com.translator.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByCode(String code);

    boolean existsByCode(String code);
}