CREATE TABLE message (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	publish_date TIMESTAMP NOT NULL,
	remove_date TIMESTAMP,
	owner VARCHAR(255) NOT NULL,
	description VARCHAR(65535) NOT NULL,
	approved_by VARCHAR(255),
	approved_date TIMESTAMP
);

CREATE TABLE users (
	username VARCHAR(50) NOT NULL,
	password VARCHAR(60) NOT NULL,
	name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	enabled SMALLINT,
	PRIMARY KEY (username)
);

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	FOREIGN KEY (username) REFERENCES users
);
