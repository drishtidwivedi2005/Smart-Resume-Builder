CREATE DATABASE smart_resume_builder;
USE smart_resume_builder;

-- Users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Resume main table
CREATE TABLE resumes (
    resume_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(100),
    objective TEXT,
    phone VARCHAR(20),
    address VARCHAR(255),
    score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- Education table
CREATE TABLE education (
    edu_id INT PRIMARY KEY AUTO_INCREMENT,
    resume_id INT,
    institute VARCHAR(150),
    degree VARCHAR(100),
    year_passed VARCHAR(10),
    percentage VARCHAR(10),
    FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
        ON DELETE CASCADE
);

-- Experience table
CREATE TABLE experience (
    exp_id INT PRIMARY KEY AUTO_INCREMENT,
    resume_id INT,
    company VARCHAR(150),
    role_name VARCHAR(100),
    years VARCHAR(20),
    description TEXT,
    FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
        ON DELETE CASCADE
);

-- Skills table
CREATE TABLE skills (
    skill_id INT PRIMARY KEY AUTO_INCREMENT,
    resume_id INT,
    skill_name VARCHAR(100),
    FOREIGN KEY (resume_id) REFERENCES resumes(resume_id)
        ON DELETE CASCADE
);


ALTER TABLE resumes
ADD COLUMN candidate_name VARCHAR(100),
ADD COLUMN email VARCHAR(100),
ADD COLUMN linkedin VARCHAR(255),
ADD COLUMN github VARCHAR(255),
ADD COLUMN skills TEXT,
ADD COLUMN education TEXT,
ADD COLUMN projects TEXT,
ADD COLUMN experience TEXT,
ADD COLUMN certifications TEXT;


DESC resumes;