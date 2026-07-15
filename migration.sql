-- MySQL Migration Script to update the existing 'employees' table

ALTER TABLE employees
ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN email_verification_otp VARCHAR(255),
ADD COLUMN email_verification_otp_expiry DATETIME,
ADD COLUMN reset_password_otp VARCHAR(255),
ADD COLUMN reset_password_otp_expiry DATETIME,
ADD COLUMN failed_login_attempts INT NOT NULL DEFAULT 0,
ADD COLUMN account_locked BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN account_locked_until DATETIME,
ADD COLUMN last_login DATETIME,
ADD COLUMN password_changed_at DATETIME;
