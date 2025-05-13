CREATE DATABASE IF NOT EXISTS concertdb;
USE concertdb;

CREATE TABLE concerts
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(100),
    date          VARCHAR(50),
    vip_seats     INT,
    regular_seats INT,
    after_party   INT
);

CREATE TABLE customers
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE reservations
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    concert_id    INT,
    seat_type     VARCHAR(20),
    customer_name VARCHAR(100),
    after_party   BOOLEAN
);
