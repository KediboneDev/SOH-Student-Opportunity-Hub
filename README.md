# SOH — Student Opportunity Hub


**Student Opportunity Hub (SOH)** is a desktop Java application developed as a university software development project. The application connects university students with companies offering bursaries and internship opportunities.

The project was developed collaboratively as part of coursework, with GitHub used for version control and team collaboration.

## Features

* User authentication with role-based access for Students and Companies
* Students can browse, filter, and apply for opportunities
* Students can manage their profiles and applications
* Companies can post opportunities and manage applicants
* Applicants can be automatically sorted according to GPA
* In-app notifications
* Data persistence using serialization
* Password hashing using BCrypt
* AES encryption for sensitive data
* Security logging

## Technologies & Concepts

* **Java**
* Object-Oriented Programming (OOP)
* Abstract classes and inheritance
* File handling and serialization
* Data structures and collections
* Exception handling
* Authentication and role-based access control
* Password hashing and encryption
* GitHub for version control and collaboration

## My Contribution

As part of the development team, I worked primarily on the student-facing functionality and opportunity management components.

My contributions included:

* Developed the Opportunity abstract class.
* Developed the Bursary and Internship subclasses.
* Developed OpportunityService for opportunity-related functionality.
* Developed StudentDashboard.
* Developed OpportunitiesScreen.
* Developed StudentProfileScreen.
* Integrated student screens with AuthService so that logged-in student information was correctly used throughout the application.
* Assisted with system integration, testing, and final submission checks.

## Project Structure

The project consists of Java classes responsible for authentication, users, opportunities, applications, notifications, security, data persistence, and the graphical user interface.

Examples include:

* AuthService.java
* Opportunity.java
* Bursary.java
* Internship.java
* OpportunityService.java
* StudentDashboard.java
* StudentProfileScreen.java
* Applications.java
* ApplicationService.java
* NotificationService.java
* PasswordUtil.java
* EncryptionUtil.java

## Academic Project

This project was completed as part of my BSc Computer Science and Mathematics studies at **North-West University**.

It provided practical experience in Java programming, object-oriented software development, teamwork, version control, system integration, and application security.

## How to Run

Open the project in BlueJ and run the `LoginScreen` JavaFX application to launch the system.

The application opens at the login screen, where users can log in or create an account and access features according to their user role.


