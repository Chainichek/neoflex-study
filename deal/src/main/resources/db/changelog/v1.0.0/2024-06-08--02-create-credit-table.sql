-- liquibase formatted sql

-- changeset daniyar:1717846384921-1
CREATE TABLE credit
(
    id                UUID           NOT NULL,
    amount            DECIMAL(19, 2) NOT NULL,
    term              INTEGER        NOT NULL,
    monthly_payment   DECIMAL(19, 2) NOT NULL,
    rate              DECIMAL(19, 2) NOT NULL,
    psk               DECIMAL(19, 2) NOT NULL,
    payment_schedule  JSONB,
    insurance_enabled BOOLEAN        NOT NULL,
    salary_client     BOOLEAN        NOT NULL,
    credit_status     VARCHAR(255)   NOT NULL,
    CONSTRAINT pk_credit PRIMARY KEY (id)
);

