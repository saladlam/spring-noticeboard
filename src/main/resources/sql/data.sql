INSERT INTO message(publish_date, remove_date, owner, description, approved_by, approved_date) VALUES ('2019-08-01 12:30:00', null, 'user1', 'Hello world', 'admin', '2019-07-31 21:00:00');
INSERT INTO message(publish_date, remove_date, owner, description, approved_by, approved_date) VALUES ('2019-08-02 14:30:00', null, 'user2', 'こんにちは', 'admin', '2019-07-31 21:00:00');
INSERT INTO message(publish_date, remove_date, owner, description, approved_by, approved_date) VALUES ('2019-07-01 00:00:00', '2019-07-14 23:59:59', 'user2', 'Expired message', 'admin', '2019-06-29 12:00:00');
INSERT INTO message(publish_date, remove_date, owner, description, approved_by, approved_date) VALUES ('2019-08-03 10:00:00', null, 'user1', 'Waiting approve message 1', null, null);
INSERT INTO message(publish_date, remove_date, owner, description, approved_by, approved_date) VALUES ('2019-08-03 10:00:00', null, 'user1', 'Waiting approve message 2', null, null);

INSERT INTO users(username, password, name, email, enabled) VALUES ('user1', '$2a$10$DVg9RjwyIV9RzVZD9t82dejVx2RYlO/fBf5yJeyFzZuWHQA1uq/za', 'Hana Setsuko', 'user1@a.co.jp', 1);
INSERT INTO users(username, password, name, email, enabled) VALUES ('user2', '$2a$10$KwYcWiGAUX3owaOECGmPHu6cGXo4XwWCvQeNE5WLLM/3M5de/i9fO', 'Kyoko Hachiro', 'user2@b.co.jp', 1);
INSERT INTO users(username, password, name, email, enabled) VALUES ('admin', '$2a$10$aDmNiyH3Baf1opRdWNK8POLor0JwoQtBqG3dLZhgQG/am7RHWeMg.', 'Yasu Takuto', 'admin@c.co.jp', 1);

INSERT INTO authorities(username, authority) VALUES ('user1', 'USER');
INSERT INTO authorities(username, authority) VALUES ('user2', 'USER');
INSERT INTO authorities(username, authority) VALUES ('admin', 'USER');
INSERT INTO authorities(username, authority) VALUES ('admin', 'ADMIN');
