# NGO Donation Tracking System

## Project Description

The NGO Donation Tracking System is a Java Full Stack web application developed using Spring Boot, JDBC, MySQL, HTML, CSS, Bootstrap, and JavaScript.

The application allows NGOs to create donation requests, donors to contribute to those requests, and administrators to monitor users, donation requests, and donations.

This project follows a simple 3-layer architecture:

Controller → Service → DAO → MySQL

---

## Technologies Used

### Frontend

- HTML5
- CSS3
- Bootstrap 5
- JavaScript
- Fetch API

### Backend

- Java 21
- Spring Boot
- Spring MVC
- JDBC

### Database

- MySQL

---

## Features

### Home

- Bootstrap Homepage
- Hero Section
- Statistics Cards
- About Section
- Image Gallery
- Footer

### Login

- Email Login
- Password
- Role Selection

### Registration

- Register as NGO
- Register as Donor
- JavaScript Validation

### Admin Dashboard

- View Users
- View Donation Requests
- View Donations

### NGO Dashboard

- Add Donation Request
- View Own Requests

### Donor Dashboard

- View Requests
- Donate

---

## REST APIs

### User

POST /register

POST /login

GET /users

### Donation Request

POST /requests

GET /requests

### Donation

POST /donate

GET /donations

---

## Database

Database Name

ngo_donation_db

Tables

- users
- donation_requests
- donations

---

## Folder Structure

NGO-Donation-System

```
frontend/
backend/
database/
README.md
```

---

## Installation Steps

1. Install Java 21 or above

2. Install MySQL

3. Create Database

```
CREATE DATABASE ngo_donation_db;
```

4. Import

```
database.sql
```

5. Configure

```
application.properties
```

6. Run

```
mvn clean install
```

```
mvn spring-boot:run
```

7. Open

```
frontend/index.html
```

