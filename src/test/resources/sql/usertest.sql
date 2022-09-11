-- password: dIw8#a-$eW
INSERT INTO users(username, password, name, email, enabled) VALUES ('user1', '$2a$12$l4gUnnQKZQK4wvN3UqyD5.eGl4Fc8qHHQpYf7z9h3pd0qB192cOsm', 'user1 user1', 'user1@user1.com', 1);

INSERT INTO authorities(username, authority) VALUES ('user1', 'USER');
