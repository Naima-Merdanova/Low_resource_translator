package com.translator.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "augmentation_jobs")
public class AugmentationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_language_id", nullable = false)
    private Language sourceLanguage;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_language_id", nullable = false)
    private Language targetLanguage;

    @Column(length = 100)
    private String domain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AugmentationStatus status;

    @Column(name = "requested_count", nullable = false)
    private int requestedCount;

    @Column(name = "created_count", nullable = false)
    private int createdCount;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected AugmentationJob() {
    }

    public AugmentationJob(
            Language sourceLanguage,
            Language targetLanguage,
            String domain,
            int requestedCount
    ) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.domain = domain;
        this.requestedCount = requestedCount;
        this.createdCount = 0;
        this.status = AugmentationStatus.PENDING;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public void markRunning() {
        this.status = AugmentationStatus.RUNNING;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCompleted(int createdCount) {
        this.status = AugmentationStatus.COMPLETED;
        this.createdCount = createdCount;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markFailed(String errorMessage) {
        this.status = AugmentationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = OffsetDateTime.now();
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

    public String getDomain() {
        return domain;
    }

    public AugmentationStatus getStatus() {
        return status;
    }

    public int getRequestedCount() {
        return requestedCount;
    }

    public int getCreatedCount() {
        return createdCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}