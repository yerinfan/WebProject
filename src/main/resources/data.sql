INSERT INTO USERS (USERNAME, PASSWORD, ROLE, FACE_REGISTERED)
VALUES 
  (
    '12',
    '$2a$10$e0MYzXyjpJS7Pd0RVvHwHeFX5bjSRBBYiF5LjYL4OeVzX1nchRZSi',  -- "1234" 암호화
    'ADMIN',
    FALSE
  ),
  (
    '11',
    '$2a$10$e0MYzXyjpJS7Pd0RVvHwHeFX5bjSRBBYiF5LjYL4OeVzX1nchRZSi',  -- 기존 유저
    'USER',
    FALSE
  );
