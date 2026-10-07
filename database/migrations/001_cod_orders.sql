-- Run on the existing database; safe to run more than once.
IF OBJECT_ID('dbo.orders', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.orders (
        order_id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL REFERENCES dbo.users(id),
        request_token VARCHAR(36) NOT NULL UNIQUE,
        recipient NVARCHAR(100) NOT NULL,
        phone VARCHAR(20) NOT NULL,
        address NVARCHAR(500) NOT NULL,
        note NVARCHAR(1000) NOT NULL DEFAULT N'',
        payment_method VARCHAR(10) NOT NULL DEFAULT 'COD' CHECK (payment_method = 'COD'),
        payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' CHECK (payment_status = 'UNPAID'),
        order_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CONSTRAINT CK_orders_status CHECK (order_status IN ('PENDING','CONFIRMED','PREPARING','SHIPPING','OUT_FOR_DELIVERY','DELIVERED','CANCELLED','RETURNED')),
        total DECIMAL(19,2) NOT NULL CHECK (total >= 0),
        created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
    );
    CREATE INDEX IX_orders_user ON dbo.orders(user_id, created_at);
END;
GO
IF OBJECT_ID('dbo.order_items', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.order_items (
        order_id BIGINT NOT NULL REFERENCES dbo.orders(order_id),
        -- Snapshot: deleting a catalog book must not delete order history.
        book_id INT NOT NULL,
        title NVARCHAR(200) NOT NULL,
        unit_price DECIMAL(19,2) NOT NULL CHECK (unit_price >= 0),
        quantity INT NOT NULL CHECK (quantity > 0),
        PRIMARY KEY (order_id, book_id)
    );
END;
GO
