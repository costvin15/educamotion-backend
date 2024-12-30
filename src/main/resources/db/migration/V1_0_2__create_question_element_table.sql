CREATE TABLE question_element (
    id VARCHAR(255),
    title VARCHAR(255),
    description VARCHAR(255),
    type VARCHAR(255) NOT NULL CHECK (type IN ('OBJECTIVE','DISCURSIVE', 'MULTIPLE_CHOICE')),
    options VARCHAR(255) ARRAY,
    correct_option VARCHAR(255),
    PRIMARY KEY (id)
);
