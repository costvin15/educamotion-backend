CREATE TABLE classroom (
    id VARCHAR(255) NOT NULL,
    presentation_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    PRIMARY KEY (id, presentation_id, user_id),
    FOREIGN KEY (presentation_id, user_id) REFERENCES presentation(id, user_id)
);
