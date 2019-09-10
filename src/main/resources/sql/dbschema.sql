CREATE TABLE message (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	publish_date TIMESTAMP NOT NULL,
	remove_date TIMESTAMP,
	owner VARCHAR(255) NOT NULL,
	description VARCHAR(65535) NOT NULL,
	approved_by VARCHAR(255),
	approved_date TIMESTAMP
);

CREATE TABLE user (
	username VARCHAR(50) NOT NULL,
	password VARCHAR(60) NOT NULL,
	enabled SMALLINT,
	PRIMARY KEY (username)
);

CREATE TABLE authority (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE user_authority (
	username VARCHAR(50) NOT NULL,
	authority_id BIGINT NOT NULL,
	FOREIGN KEY (username) REFERENCES user,
	FOREIGN KEY (authority_id) REFERENCES authority
);
