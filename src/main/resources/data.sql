-- src/main/resources/data.sql

INSERT INTO USERS (USERNAME, PASSWORD, ROLE, FACE_REGISTERED)
VALUES 
  ('admin', '$2a$10$5rBfszKHMTcfNPVoO7Cw9uM1QxjK1J1cAzTxOjTi4QoQ7GfNbnHF6', 'ADMIN', FALSE),
  ('user',  '$2a$10$uQuz8W2sgAyat1QxR3K6HeRYC8/UOrkF7zTyX/Ydt9s0xTcVNHhOq', 'USER',  FALSE);
