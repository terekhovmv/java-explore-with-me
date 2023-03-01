CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255)                            NOT NULL,

    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,

    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    title              VARCHAR(100)                            NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    location_lat       REAL                                    NOT NULL,
    location_lon       REAL                                    NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    state              VARCHAR(64)                             NOT NULL,
    confirmed_requests BIGINT                                  NOT NULL,
    cached_views       BIGINT                                  NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_initiator FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(64)                             NOT NULL,
    created_on   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,

    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT uq_request_event_requester UNIQUE (event_id, requester_id),
    CONSTRAINT fk_request_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_request_requester FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR                                 NOT NULL,

    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,

    CONSTRAINT pk_compilation_event PRIMARY KEY (compilation_id, event_id),
    FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);