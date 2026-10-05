-- Existing accounts are upgraded to PBKDF2 on their next successful login.
ALTER TABLE dbo.users ALTER COLUMN passwd VARCHAR(255) NOT NULL;
GO
