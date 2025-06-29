CREATE TABLE IF NOT EXISTS USERS (
  ID BIGINT PRIMARY KEY,
  USERNAME VARCHAR(255) UNIQUE,
  PASSWORD VARCHAR(255),
  ROLE VARCHAR(50),
  FACE_REGISTERED BOOLEAN DEFAULT FALSE -- 👈 여기로 통합 가능
);
CREATE SEQUENCE IF NOT EXISTS SEQ_USERS START WITH 1;

-- ❌ 중복이므로 제거하세요
-- ALTER TABLE USERS ADD FACE_REGISTERED BOOLEAN DEFAULT FALSE;
-- src/main/resources/data.sql

INSERT INTO USERS (USERNAME, PASSWORD, ROLE, FACE_REGISTERED)
VALUES 
  (
    'admin',
    '$2a$10$e0MYzXyjpJS7Pd0RVvHwHeFX5bjSRBBYiF5LjYL4OeVzX1nchRZSi',  -- "1234" 암호화
    'ADMIN',
    FALSE
  ),
  (
    'user',
    '$2a$10$e0MYzXyjpJS7Pd0RVvHwHeFX5bjSRBBYiF5LjYL4OeVzX1nchRZSi',  -- 기존 유저
    'USER',
    FALSE
  ),
  (
    'test',
    '$2a$10$e0MYzXyjpJS7Pd0RVvHwHeFX5bjSRBBYiF5LjYL4OeVzX1nchRZSi',  -- 기존 유저
    'USER',
    FALSE
  );
