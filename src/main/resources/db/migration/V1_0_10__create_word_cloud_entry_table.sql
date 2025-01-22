CREATE TABLE word_cloud_entry (
    id VARCHAR(255) NOT NULL,
    word_cloud_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    entry VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (word_cloud_id) REFERENCES word_cloud_element(id)
);
