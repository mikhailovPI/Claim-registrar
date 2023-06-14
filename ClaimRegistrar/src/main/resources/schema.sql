drop table requests cascade;
--drop table users cascade;
--drop table roles cascade;
--drop table users_roles cascade;

CREATE TABLE IF NOT EXISTS roles
(
    role_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    role_name VARCHAR(32)                             NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name     VARCHAR(256)                            NOT NULL,
    user_password VARCHAR(128)                            NOT NULL,
    email         VARCHAR(128)                            NOT NULL,
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

CREATE TABLE IF NOT EXISTS users_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_role_id_users_roles FOREIGN KEY (role_id) REFERENCES roles (role_id),
    CONSTRAINT fk_user_id_users_roles FOREIGN KEY (user_id) REFERENCES users (user_id)
);