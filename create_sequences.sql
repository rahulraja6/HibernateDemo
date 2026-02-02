-- Oracle Sequence Creation Script
-- Execute these statements in your Oracle database to create the sequences
-- required for entity ID generation

-- Sequence for Customer table
CREATE SEQUENCE CUSTOMER_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence for Account table
CREATE SEQUENCE ACCOUNT_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence for KYC Profile table
CREATE SEQUENCE KYC_PROFILE_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence for Bank Transaction table
CREATE SEQUENCE BANK_TRANSACTION_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence for Reference Notification Channel table
CREATE SEQUENCE REF_NOTIFICATION_CHANNEL_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Optional: If you already have data in the tables, set the sequence start value
-- to be higher than the maximum existing ID value. Example:
-- ALTER SEQUENCE CUSTOMER_SEQ RESTART WITH 1000;

-- Verify sequences were created successfully
SELECT sequence_name, last_number 
FROM user_sequences 
WHERE sequence_name IN (
    'CUSTOMER_SEQ',
    'ACCOUNT_SEQ', 
    'KYC_PROFILE_SEQ',
    'BANK_TRANSACTION_SEQ',
    'REF_NOTIFICATION_CHANNEL_SEQ'
);
