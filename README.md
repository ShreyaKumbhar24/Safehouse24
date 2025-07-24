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

<img width="1604" height="784" alt="image" src="https://github.com/user-attachments/assets/6a6777ea-a2e2-40aa-bfb7-3a15e860e2ce" />


This is the user registration interface where new users can sign up by entering their credentials, email, phone number, and vault name. The form ensures data validation before submission, enabling account creation securely.

<img width="1604" height="795" alt="image" src="https://github.com/user-attachments/assets/7260c579-37d4-47c1-a5de-15573c12109b" />
<img width="1604" height="627" alt="image" src="https://github.com/user-attachments/assets/ffae7722-0dd4-47ce-a227-269ac61973ec" />


Once a user registers, a One-Time Password (OTP) is generated and sent to their registered email address for verification. This ensures secure authentication and prevents unauthorized access.

<img width="1604" height="822" alt="image" src="https://github.com/user-attachments/assets/3fb97370-10c0-4395-91e9-00fff535e222" />


This is the login page where users access the application by entering their username and password. It provides links for account registration and password recovery, ensuring easy access management.

<img width="1604" height="791" alt="image" src="https://github.com/user-attachments/assets/98ef991e-10ab-47aa-9c4a-e1bc7f0b249b" />


After logging in, users are prompted to enter their secret vault name. This adds a secondary layer of security. Upon successful input, the user gains access to their encrypted digital vault.

<img width="1604" height="834" alt="image" src="https://github.com/user-attachments/assets/cfaa6439-c658-4273-bfb1-4ebfa6bdf15f" />


This is the main vault dashboard where users can securely add, view, and manage encrypted notes, images, pdf and stored passwords. The clean UI promotes usability while safeguarding sensitive data.



##Conclusion:
SafeHouse24 is a secure, scalable, and user-centric web application built with modern Java technologies. It not only provides users with a protected digital space for sensitive information but also incorporates key principles of cybersecurity including multi-step authentication, encrypted storage, and safe session handling. With intuitive design and practical features, it’s a great solution for personal data management in today’s digital age.

Future scope includes:

End-to-end encryption for stored data

Session timeout and inactivity logout

Advanced vault categories (documents, media, etc.)

Mobile-responsive UI using frameworks like Bootstrap

Author
Shreya Kumbhar
 Milwaukee, WI |  Software Engineer |  MS in Computer Science
 LinkedIn |  shreyaaa024@gmail.com


