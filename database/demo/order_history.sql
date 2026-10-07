-- Demo records only, for user@gmail.com. No inventory is deducted.
-- Run after migration 004; repeated execution does not duplicate demo orders.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
DECLARE @userId INT = (SELECT id FROM dbo.users WHERE email='user@gmail.com');
DECLARE @bookId INT, @price DECIMAL(19,2), @title NVARCHAR(200);
SELECT TOP (1) @bookId=bookid, @price=price, @title=title FROM dbo.books WHERE price>=0 ORDER BY bookid;
IF @userId IS NULL OR @bookId IS NULL THROW 51000, 'Demo user or book is missing.', 1;
DECLARE @states TABLE (code VARCHAR(20));
INSERT INTO @states VALUES ('PENDING'),('CONFIRMED'),('PREPARING'),('SHIPPING'),
    ('OUT_FOR_DELIVERY'),('DELIVERED'),('CANCELLED'),('RETURNED');
DECLARE @code VARCHAR(20), @note NVARCHAR(1000), @id BIGINT;
DECLARE demo_cursor CURSOR LOCAL FAST_FORWARD FOR SELECT code FROM @states;
OPEN demo_cursor;
FETCH NEXT FROM demo_cursor INTO @code;
WHILE @@FETCH_STATUS=0
BEGIN
    SET @note=N'[DEMO_HISTORY:' + @code + N'] Đơn minh họa bộ lọc trạng thái, không phải đơn bán hàng thực tế.';
    IF NOT EXISTS (SELECT 1 FROM dbo.orders WHERE user_id=@userId AND note=@note)
    BEGIN
        INSERT INTO dbo.orders(user_id,request_token,recipient,phone,address,note,total,order_status)
        VALUES(@userId,CONVERT(VARCHAR(36),NEWID()),N'Khách minh họa lịch sử','0901234567',N'Địa chỉ mẫu để kiểm tra lịch sử đặt hàng',@note,@price,@code);
        SET @id=SCOPE_IDENTITY();
        INSERT INTO dbo.order_items(order_id,book_id,title,unit_price,quantity) VALUES(@id,@bookId,@title,@price,1);
    END;
    FETCH NEXT FROM demo_cursor INTO @code;
END;
CLOSE demo_cursor;
DEALLOCATE demo_cursor;
COMMIT;
SELECT order_id,order_status,note FROM dbo.orders WHERE user_id=@userId AND note LIKE N'[[]DEMO_HISTORY:%' ORDER BY order_id;
GO
