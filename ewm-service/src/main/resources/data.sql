DELETE
FROM requests;
DELETE
FROM events;
DELETE
FROM users;
DELETE
FROM categories;

ALTER TABLE users
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE categories
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO users(name, email)
VALUES ('ann', 'ann@abc.def'),
       ('bob', 'bob@abc.def'),
       ('cam', 'cam@abc.def');

INSERT INTO categories(name)
VALUES ('anschlag'),
       ('business'),
       ('concerts');
