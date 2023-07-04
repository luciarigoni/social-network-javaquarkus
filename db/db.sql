CREATE DATABASE quarkus;

CREATE TABLE User (
	id int NOT NULL AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	age int NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE Posts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_text varchar(150) NOT NULL,
    dateTime DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id),
    PRIMARY KEY (id)
);

