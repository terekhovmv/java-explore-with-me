DELETE
FROM hits;
DELETE
FROM uris;
DELETE
FROM apps;

ALTER TABLE apps
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE uris
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE hits
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO apps(name)
VALUES ('app1'),
       ('app2');
INSERT INTO uris(path)
VALUES ('uri1'),
       ('uri2'),
       ('uri3');
INSERT INTO hits(app_id, uri_id, ip)
VALUES (1, 1, '127.0.0.1'),
       (1, 1, '127.0.0.1'),
       (1, 2, '127.0.0.1'),
       (1, 3, '127.0.0.1'),
       (1, 1, '192.168.1.1'),
       (2, 3, '127.0.0.1'),
       (2, 3, '192.168.1.1');
