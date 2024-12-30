CREATE TABLE element (
    id VARCHAR(255),
    presentation_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    slide_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) CHECK (type IN ('QUESTION','POLL','WORD_CLOUD','COSMO')),
    positionX INT DEFAULT 0,
    positionY INT DEFAULT 0,
    width INT DEFAULT 0,
    height INT DEFAULT 0,
    PRIMARY KEY (id, presentation_id, user_id, slide_id),
    FOREIGN KEY (presentation_id, user_id) REFERENCES presentation(id, user_id)
);
