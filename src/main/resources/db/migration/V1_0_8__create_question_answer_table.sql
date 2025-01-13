CREATE TABLE question_answer (
    question_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    answer VARCHAR(255) NOT NULL,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    correct BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (question_id, user_id),
    FOREIGN KEY (question_id) REFERENCES question_element(id)
);
