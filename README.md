# SafeHouse24 

## Introduction

### Application Overview
SafeHouse24 is a secure and user-friendly web application designed to safeguard private information while providing seamless access 24/7. The application is built using Java servlets for the backend, with frontend components developed in HTML, CSS, and JavaScript. It runs on an Apache Tomcat server.

The primary goals of this project include:

- Implementing robust user registration and login with secure password hashing.
- Integrating OTP verification via email using SMTP for enhanced security.
- Developing a digital vault feature that allows users to securely store notes, passwords, images, and documents.
- Ensuring secure session management and data handling throughout the application.
- Delivering a responsive and intuitive user interface for an optimal user experience.

##  Features Implementation

###  User Registration and Login

- SHA-256 password hashing
- Email-based OTP verification
- Secure validation and storage of user data

###  Digital Vault

- Access vault using a private vault name
- Create, edit, and delete notes/passwords
- Backend servlet handles vault logic and storage

---

## 🛡 Security Enhancements

- Client- and server-side input validation
- Protection from SQL Injection and XSS attacks
- File type checks and sanitization on uploads

---

##  Testing

- Unit Testing using **JUnit** and **Mockito**
- Black-box testing of login, OTP, and vault flows
- Penetration testing using **OWASP ZAP**
- Static code analysis with **Coverity Scan**
- CI integration using **Travis CI**
