-- Allow all eight history states while keeping existing PENDING orders.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
DECLARE @dropChecks NVARCHAR(MAX) = N'';
SELECT @dropChecks = @dropChecks + N'ALTER TABLE dbo.orders DROP CONSTRAINT ' + QUOTENAME(name) + N';'
FROM sys.check_constraints
WHERE parent_object_id = OBJECT_ID(N'dbo.orders') AND definition LIKE N'%order_status%';
EXEC sp_executesql @dropChecks;
ALTER TABLE dbo.orders WITH CHECK ADD CONSTRAINT CK_orders_status
    CHECK (order_status IN ('PENDING','CONFIRMED','PREPARING','SHIPPING',
        'OUT_FOR_DELIVERY','DELIVERED','CANCELLED','RETURNED'));
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id=OBJECT_ID(N'dbo.orders') AND name=N'IX_orders_user_status')
    CREATE INDEX IX_orders_user_status ON dbo.orders(user_id, order_status, created_at DESC, order_id DESC);
COMMIT;
GO
