-- Preserve Vietnamese text and support normal VND prices.
ALTER TABLE dbo.books ALTER COLUMN title NVARCHAR(200) NULL;
ALTER TABLE dbo.books ALTER COLUMN publisher NVARCHAR(100) NULL;
ALTER TABLE dbo.books ALTER COLUMN description NVARCHAR(MAX) NULL;
ALTER TABLE dbo.books ALTER COLUMN cover_image NVARCHAR(2048) NULL;
ALTER TABLE dbo.books ALTER COLUMN price DECIMAL(19,2) NULL;
ALTER TABLE dbo.author ALTER COLUMN author_name NVARCHAR(100) NULL;
ALTER TABLE dbo.rating ALTER COLUMN review_text NVARCHAR(MAX) NULL;
GO
