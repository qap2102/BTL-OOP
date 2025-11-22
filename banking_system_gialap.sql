CREATE DATABASE IF NOT EXISTS bankdb;
USE bankdb;

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARBINARY(255) NOT NULL,
    citizen_id VARCHAR(50) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    sex ENUM('MALE','FEMALE','OTHER'),
    nationality VARCHAR(255),
    place_of_origin VARCHAR(255),
    place_of_residence VARCHAR(255),
    email VARCHAR(100)UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance DECIMAL(19,4) NOT NULL DEFAULT 0.0000,
    pin_smart VARBINARY(255) NOT NULL,
    account_status ENUM('ACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    from_account_id INT,
    to_account_id INT,
    amount DECIMAL(19,4) NOT NULL DEFAULT 0.0000,
    transaction_type ENUM('TRANSFER','DEPOSIT','WITHDRAW') NOT NULL,
    transaction_content VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_status ENUM('SUCCESS','FAILED') NOT NULL,
    FOREIGN KEY (from_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL,
    FOREIGN KEY (to_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(50) DEFAULT 'Quản trị viên',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);