drop table requests;
drop table users;


CREATE TABLE IF NOT EXISTS users
(
    user_id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name     VARCHAR(256)                            NOT NULL,
    user_password VARCHAR(128)                            NOT NULL,
    email         VARCHAR(128)                            NOT NULL,
    user_role     VARCHAR(32)                             DEFAULT 'USER',
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text_request VARCHAR(1024)                           NOT NULL,
    created_on   TIMESTAMP                               NOT NULL,
    user_id      BIGINT                                  NOT NULL,
    status       VARCHAR(32)                             NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (request_id),
    CONSTRAINT fk_user_id_requests FOREIGN KEY (user_id) REFERENCES users (user_id)
);
