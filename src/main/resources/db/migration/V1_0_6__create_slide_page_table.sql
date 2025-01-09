CREATE TABLE slide_page (
    presentation_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    slide_id VARCHAR(255) NOT NULL,
    slide_index INTEGER DEFAULT 0,
    stored_filename VARCHAR(255) NOT NULL,
    PRIMARY KEY (presentation_id, user_id, slide_id),
    FOREIGN KEY (presentation_id, user_id) REFERENCES presentation(id, user_id)
);
