CREATE DATABASE ngo_donation_db;

USE ngo_donation_db;

CREATE TABLE users(

id INT PRIMARY KEY AUTO_INCREMENT,

name VARCHAR(100) NOT NULL,

email VARCHAR(100) UNIQUE NOT NULL,

password VARCHAR(100) NOT NULL,

role VARCHAR(20) NOT NULL

);

CREATE TABLE donation_requests(

id INT PRIMARY KEY AUTO_INCREMENT,

ngo_id INT NOT NULL,

title VARCHAR(150) NOT NULL,

description TEXT,

target_amount DOUBLE NOT NULL,

collected_amount DOUBLE DEFAULT 0,

status VARCHAR(30) DEFAULT 'ACTIVE',

FOREIGN KEY(ngo_id)

REFERENCES users(id)

);

CREATE TABLE donations(

id INT PRIMARY KEY AUTO_INCREMENT,

donor_id INT NOT NULL,

request_id INT NOT NULL,

amount DOUBLE NOT NULL,

donation_date DATE,

FOREIGN KEY(donor_id)

REFERENCES users(id),

FOREIGN KEY(request_id)

REFERENCES donation_requests(id)

);

INSERT INTO users(name,email,password,role)

VALUES

('Admin','admin@donation.org','admin123','ADMIN'),

('Rahul Kumar','rahul@gmail.com','123','DONOR'),

('Priya Sharma','priya@gmail.com','123','DONOR'),

('Helping Hands NGO','ngo@gmail.com','123','NGO');

INSERT INTO donation_requests

(ngo_id,title,description,target_amount,collected_amount,status)

VALUES

(4,

'Education Support',

'Help poor students with school fees',

50000,

12000,

'ACTIVE');

INSERT INTO donations

(donor_id,request_id,amount,donation_date)

VALUES

(2,1,5000,CURDATE()),

(3,1,7000,CURDATE());