package com.translator.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "translation_examples")
public class TranslationExample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_language_id", nullable = false)
    private Language sourceLanguage;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_language_id", nullable = false)
    private Language targetLanguage;

    @Column(name = "source_text", nullable = false, columnDefinition = "text")
    private String sourceText;

    @Column(name = "target_text", nullable = false, columnDefinition = "text")
    private String targetText;

    @Column(length = 100)
    private String domain;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected TranslationExample() {
    }

    public TranslationExample(
            Language sourceLanguage,
            Language targetLanguage,
            String sourceText,
            String targetText,
            String domain
    ) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.sourceText = sourceText;
        this.targetText = targetText;
        this.domain = domain;
        this.createdAt = OffsetDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getTargetText() {
        return targetText;
    }

    public String getDomain() {
        return domain;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}