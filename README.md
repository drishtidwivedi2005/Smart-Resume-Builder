# Smart Resume Builder

A professional Java desktop application built using **Java Swing, MySQL, JDBC, Git, GitHub, and JUnit Testing**.  
This project helps users create ATS-friendly resumes, manage saved resumes, analyze resume quality, and export resumes professionally.

---

## Features

### User Authentication
- User Registration
- Secure Login System
- Logout Option

### Resume Builder
- Create professional ATS-friendly resumes
- Custom Resume Name
- Sections Included:
  - Full Name
  - Email
  - Phone
  - LinkedIn
  - GitHub
  - Summary
  - Skills
  - Education
  - Projects
  - Experience
  - Certifications

### Saved Resume Manager
- View all saved resumes
- Search resumes
- Rename resume title
- Delete resumes
- Live Preview Resume
- Export Resume

### ATS Resume Analyzer
- Analyze saved resumes
- Generate ATS Score out of 100
- Resume Rating:
  - Excellent
  - Strong
  - Average
  - Needs Improvement
- Keyword Detection
- Improvement Suggestions

### UI Enhancements
- Dark Mode / Light Mode
- Professional Dashboard
- Real-time Statistics

### Dashboard Analytics
- Total Resumes
- Average ATS Score
- Interview Ready Count

---

## Technologies Used

- Java
- Java Swing
- MySQL
- JDBC
- Git
- GitHub
- JUnit 5

---

## Database Design

Main Tables:

- users
- resumes
- education
- experience
- skills

Includes:
- Primary Keys
- Foreign Keys
- Cascading Deletes

---

## Testing Implemented

### Unit Testing
- Login Validation Test
- ATS Score Test

### Integration Testing
- Database Connection Test

All tests passed successfully using JUnit.

---

## Project Structure

```text
Smart-Resume-Builder/
│── src/
│   ├── ui/
│   ├── db/
│   ├── util/
│   └── test/
│
│── lib/
│── sql/
│── README.md
