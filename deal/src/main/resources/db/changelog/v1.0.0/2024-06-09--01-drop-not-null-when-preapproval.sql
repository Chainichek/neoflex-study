-- liquibase formatted sql

-- changeset daniyar:1717881693429-1
ALTER TABLE client
    ALTER COLUMN account_number DROP NOT NULL;

-- changeset daniyar:1717881693429-2
ALTER TABLE client
    ALTER COLUMN dependent_amount DROP NOT NULL;

-- changeset daniyar:1717881693429-3
ALTER TABLE client
    ALTER COLUMN employment DROP NOT NULL;

-- changeset daniyar:1717881693429-4
ALTER TABLE client
    ALTER COLUMN gender DROP NOT NULL;

-- changeset daniyar:1717881693429-5
ALTER TABLE client
    ALTER COLUMN marital_status DROP NOT NULL;

-- changeset daniyar:1717881693429-6
ALTER TABLE statement
    ALTER COLUMN credit_id DROP NOT NULL;


