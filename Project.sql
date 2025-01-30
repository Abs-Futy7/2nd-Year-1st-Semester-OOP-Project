create database schoolmanagementsystem;

use schoolmanagementsystem;
 
CREATE TABLE register (
	id INT auto_increment primary key,
    FullName VARCHAR(100),
    Roll INT UNIQUE,
    dob VARCHAR(100),
    address TEXT,
    phone VARCHAR(15),
    email VARCHAR(100),
    bc VARCHAR(50),
    username VARCHAR(100),
    password VARCHAR(100),
    image LONGBLOB NOT NULL,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from register;
 
 

CREATE TABLE student (
    name VARCHAR(100) NOT NULL,
    fname VARCHAR(100),  -- Father's name
    rollno int NOT NULL UNIQUE,  -- Roll number, unique identifier
    dob VARCHAR(100),  -- Date of Birth
    address TEXT,  -- Address
    phone VARCHAR(15),  -- Phone number
    email VARCHAR(100),  -- Email address
    bc VARCHAR(50), -- birth certificate no
    class varchar(50)
);

 

 
select * from student;
 
 
CREATE TABLE teacher (
    name VARCHAR(100) NOT NULL,
    fname VARCHAR(100),  -- Father's name
    id int NOT NULL UNIQUE,  -- Roll number, unique identifier
    dob VARCHAR(100),  -- Date of Birth
    address TEXT,  -- Address
    phone VARCHAR(15),  -- Phone number
    email VARCHAR(100),  -- Email address
    nid VARCHAR(50)  -- birth certificate
);

select*from teacher;
 

CREATE TABLE attendance_records (
    roll_number INT,
    class VARCHAR(10),
    subject VARCHAR(50),
    date DATE,
    attendance VARCHAR(2)
);

 
SELECT * FROM attendance_records;
 
 

CREATE TABLE materials (
    id INT AUTO_INCREMENT PRIMARY KEY,
    class varchar(20),
    subject VARCHAR(100),
    file_type VARCHAR(50),
    drive_link TEXT,
    description TEXT
);


SELECT * FROM materials;

CREATE TABLE assignclass (
    id INT,
    teacher_name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    class VARCHAR(10) NOT NULL,
    time VARCHAR(20) NOT NULL,
    classroom VARCHAR(20) NOT NULL,
    UNIQUE KEY unique_assignment (subject, class, time, classroom)
);

select*from assignclass;

CREATE TABLE paymentfields (
    id INT,
    exam_fee DECIMAL(10,2) DEFAULT 0.00,
    lab_fee DECIMAL(10,2) DEFAULT 0.00,
    session_fee DECIMAL(10,2) DEFAULT 0.00,
    other_fee DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) GENERATED ALWAYS AS 
        (exam_fee + lab_fee + session_fee + other_fee) STORED
);

select * from paymentfields;
CREATE TABLE payment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    roll INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    due DECIMAL(10,2) ,
    payment_date DATE NOT NULL
);
 
select * from payment;





 
