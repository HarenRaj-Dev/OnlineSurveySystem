CREATE DATABASE online_survey;
USE online_survey;

CREATE TABLE responses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    question VARCHAR(255),
    answer TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
