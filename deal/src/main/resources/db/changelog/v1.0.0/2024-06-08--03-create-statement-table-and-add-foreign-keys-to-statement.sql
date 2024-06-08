-- liquibase formatted sql

-- changeset daniyar:1717861470153-1
CREATE TABLE statement
(
    id             UUID                        NOT NULL,
    client_id      UUID                        NOT NULL,
    credit_id      UUID                        NOT NULL,
    status         VARCHAR(255)                NOT NULL,
    creation_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    applied_offer  JSONB,
    sign_date      TIMESTAMP WITHOUT TIME ZONE,
    ses_code       VARCHAR(255),
    status_history JSONB                       NOT NULL,
    CONSTRAINT pk_statement PRIMARY KEY (id)
);

-- changeset daniyar:1717861470153-2
ALTER TABLE statement
    ADD CONSTRAINT uc_statement_client UNIQUE (client_id);

-- changeset daniyar:1717861470153-3
ALTER TABLE statement
    ADD CONSTRAINT uc_statement_credit UNIQUE (credit_id);

-- changeset daniyar:1717861470153-4
ALTER TABLE statement
    ADD CONSTRAINT FK_STATEMENT_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client (id);

-- changeset daniyar:1717861470153-5
ALTER TABLE statement
    ADD CONSTRAINT FK_STATEMENT_ON_CREDIT FOREIGN KEY (credit_id) REFERENCES credit (id);

