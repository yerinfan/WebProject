-- 예: username = admin, password = password123, role = ADMIN
INSERT INTO USERS (ID, USERNAME, PASSWORD, ROLE) VALUES
  (SEQ_USERS.NEXTVAL, 'admin', '$2a$10$7J0qiV4IvUfAuwxlDH3XeuRZQxXhQ5tv5tqz9LobwW9K/11HeARQe', 'ADMIN');
