CREATE TABLE languages (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(20) NOT NULL UNIQUE,
                           name VARCHAR(100) NOT NULL,
                           created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE translation_examples (
                                      id BIGSERIAL PRIMARY KEY,
                                      source_language_id BIGINT NOT NULL REFERENCES languages(id),
                                      target_language_id BIGINT NOT NULL REFERENCES languages(id),
                                      source_text TEXT NOT NULL,
                                      target_text TEXT NOT NULL,
                                      domain VARCHAR(100),
                                      created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE augmentation_jobs (
                                   id BIGSERIAL PRIMARY KEY,
                                   source_language_id BIGINT NOT NULL REFERENCES languages(id),
                                   target_language_id BIGINT NOT NULL REFERENCES languages(id),
                                   domain VARCHAR(100),
                                   status VARCHAR(30) NOT NULL,
                                   requested_count INTEGER NOT NULL,
                                   created_count INTEGER NOT NULL,
                                   error_message TEXT,
                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                   updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_translation_examples_languages
    ON translation_examples(source_language_id, target_language_id);

CREATE INDEX idx_translation_examples_domain
    ON translation_examples(domain);

CREATE INDEX idx_augmentation_jobs_source_language
    ON augmentation_jobs(source_language_id);

CREATE INDEX idx_augmentation_jobs_target_language
    ON augmentation_jobs(target_language_id);