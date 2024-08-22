-- liquibase formatted sql

-- changeset daniyar:1718203308503-1
ALTER TABLE client
    ALTER COLUMN account_number TYPE VARCHAR(20) USING (account_number::VARCHAR(20));

