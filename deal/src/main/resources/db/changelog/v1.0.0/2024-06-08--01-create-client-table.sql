-- liquibase formatted sql

-- changeset daniyar:1717839438944-1
CREATE TABLE client
(
    id               UUID         NOT NULL,
    last_name        VARCHAR(30)  NOT NULL,
    first_name       VARCHAR(30)  NOT NULL,
    middle_name      VARCHAR(30),
    birthdate        date         NOT NULL,
    email            VARCHAR(320) NOT NULL,
    gender           VARCHAR(255) NOT NULL,
    marital_status   VARCHAR(255) NOT NULL,
    dependent_amount INTEGER      NOT NULL,
    passport         JSONB        NOT NULL,
    employment       JSONB        NOT NULL,
    account_number   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);
