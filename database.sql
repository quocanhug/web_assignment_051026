-- 1. TẠO CƠ SỞ DỮ LIỆU
CREATE DATABASE WebExamDB;
GO
USE WebExamDB;
GO


-- 3. TẠO BẢNG books
CREATE TABLE books (
    bookid INT IDENTITY(1,1) PRIMARY KEY,
    isbn INT NULL,
    title NVARCHAR(200) NULL,
    publisher NVARCHAR(100) NULL,
    price DECIMAL(19, 2) NULL,
    description NVARCHAR(MAX) NULL,
    publish_date DATE NULL,
    cover_image NVARCHAR(2048) NULL,
    quantity INT NULL
);
GO

-- 4. TẠO BẢNG users
CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    fullname NVARCHAR(50) NULL,
    phone INT NULL,
    passwd VARCHAR(255) NOT NULL,
    signup_date DATETIME NULL DEFAULT GETDATE(),
    last_login DATETIME NULL,
    is_admin BIT NULL DEFAULT 0
);
GO

-- 5. TẠO BẢNG author
CREATE TABLE author (
    author_id INT IDENTITY(1,1) PRIMARY KEY,
    author_name NVARCHAR(100) NULL,
    date_of_birth DATE NULL
);
GO

-- 6. TẠO BẢNG book_author (Quan hệ nhiều - nhiều giữa books và author)
CREATE TABLE book_author (
    bookid INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (bookid, author_id),
    CONSTRAINT FK_BookAuthor_Book FOREIGN KEY (bookid) REFERENCES books(bookid) ON DELETE CASCADE,
    CONSTRAINT FK_BookAuthor_Author FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE CASCADE
);
GO

-- 7. TẠO BẢNG rating (Quan hệ nhiều - nhiều giữa users và books)
CREATE TABLE rating (
    userid INT NOT NULL,
    bookid INT NOT NULL,
    rating TINYINT NULL,
    review_text NVARCHAR(MAX) NULL,
    PRIMARY KEY (userid, bookid),
    CONSTRAINT FK_Rating_User FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_Rating_Book FOREIGN KEY (bookid) REFERENCES books(bookid) ON DELETE CASCADE
);
GO

-- ========================================================
-- CHÈN DỮ LIỆU MẪU (SEED DATA)
-- ========================================================

-- 1. Chèn Users (Tài khoản mẫu: admin@gmail.com / 123456 và user@gmail.com / 123456)
INSERT INTO users (email, fullname, phone, passwd, signup_date, is_admin)
VALUES
('admin@gmail.com', N'Quản trị viên', 987654321, '123456', GETDATE(), 1),
('user@gmail.com', N'Đinh Quốc Anh', 912345678, '123456', GETDATE(), 0),
('hoangnam@gmail.com', N'Nguyễn Hoàng Nam', 933221100, '123456', GETDATE(), 0),
('thanhhang@gmail.com', N'Trần Thị Thanh Hằng', 944556677, '123456', GETDATE(), 0);
GO

-- 2. Chèn Authors
INSERT INTO author (author_name, date_of_birth)
VALUES
('Nguyen Nhat Anh', '1955-05-07'),
('J.K. Rowling', '1965-07-31'),
('Haruki Murakami', '1949-01-12'),
('Dale Carnegie', '1888-11-24');
GO

-- 3. Chèn Books (Tối thiểu 4-5 sách cho mỗi tác giả để test phân trang 3sp/trang)
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
VALUES
-- Sách của Nguyễn Nhật Ánh (Tác giả 1)
(1001, 'Mat Biec', 'NXB Tre', 110.00, 'Cau chuyen tinh dep va buon giua Ngan va Ha Lan o lang Do.', '2019-09-15', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500', 50),
(1002, 'Cho Toi Xin Mot Ve Di Tuoi Tho', 'NXB Tre', 85.00, 'Cuon sach dua doc gia tro ve voi nhung ky niem thoi tho au trong treo va hon nhien.', '2018-03-20', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500', 40),
(1003, 'Toi Thay Hoa Vang Tren Co Xanh', 'NXB Tre', 125.00, 'Tam ly tuoi moi lon qua loi ke chan thanh, moc mac cua cau be Thieu.', '2020-01-10', 'https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=500', 35),
(1004, 'Ngoi Khoc Tren Cay', 'NXB Tre', 95.00, 'Cau chuyen tinh day cam dong giua Dong va Rua voi boi canh thien nhien tuyet dep.', '2021-06-12', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500', 25),
(1005, 'Co Gai Den Tu Hom Qua', 'NXB Tre', 90.00, 'Nhung rung dong ngot ngao cua tinh yeu hoc tro day mo mong.', '2017-08-05', 'https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=500', 30),

-- Sách của J.K. Rowling (Tác giả 2)
(2001, 'Harry Potter va Hon Da Phu Thuy', 'Bloomsbury', 150.00, 'Cuoc phieu luu dau tien cua cau be phu thuy Harry Potter tai truong Hogwarts.', '2015-06-26', 'https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=500', 60),
(2002, 'Harry Potter va Phong Chua Bi Mat', 'Bloomsbury', 160.00, 'Nhung bi an rung ron trong ngoi truong Hogwarts o nam hoc thu hai.', '2016-07-02', 'https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=500', 45),
(2003, 'Harry Potter va Ten Tu Nhan Nguc Azkaban', 'Bloomsbury', 170.00, 'Cuoc hoi ngo giua Harry Potter va nguoi cha do dau Sirius Black.', '2017-07-08', 'https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=500', 50),
(2004, 'Harry Potter va Chiec Coc Lua', 'Bloomsbury', 190.00, 'Giai dau Tam Phap Thuat khoc liet va su tro lai cua Chua te hac am.', '2018-07-08', 'https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=500', 40),

-- Sách của Haruki Murakami (Tác giả 3)
(3001, 'Rung Na Uy', 'NXB Hoi Nha Van', 140.00, 'Tac pham noi tieng ve su co don va noi dau mat mat cua tuoi tre.', '2016-10-15', 'https://images.unsplash.com/photo-1541963463532-d68292c34b19?w=500', 20),
(3002, 'Kafka Ben Bo Bien', 'NXB Hoi Nha Van', 165.00, 'Hanh trinh tim kiem ban nga ky la giua hien thuc va the gioi sieu nhien.', '2019-11-20', 'https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=500', 15),
(3003, '1Q84', 'NXB Hoi Nha Van', 250.00, 'The gioi song song day bi an cua Aomame va Tengo trong nam 1984 ky la.', '2020-05-18', 'https://images.unsplash.com/photo-1491841573634-28140fc7ced7?w=500', 18);
GO

-- 4. Chèn quan hệ book_author
INSERT INTO book_author (bookid, author_id)
VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), -- Sách Nguyễn Nhật Ánh
(6, 2), (7, 2), (8, 2), (9, 2),         -- Sách J.K. Rowling
(10, 3), (11, 3), (12, 3);              -- Sách Haruki Murakami
GO

-- 5. Chèn Rating / Review (theo đúng format đề bài [users]: [review_text])
INSERT INTO rating (userid, bookid, rating, review_text)
VALUES
(2, 1, 5, 'Cuon sach qua tuyet voi, doan ket khien toi rat xuc dong!'),
(3, 1, 5, 'Van phong Nguyen Nhat Anh luon nhe nhang va sau lang.'),
(4, 1, 4, 'Mot tac pham kinh dien ve tinh yeu thoi hoc sinh.'),
(2, 2, 5, 'Doc xong nho thoi tho au que nha ghe gap.'),
(3, 2, 4, 'Hai huoc, tinh te va day y nghia.'),
(2, 3, 5, 'Thao thuc voi nhung trang van day chat tho.'),
(2, 6, 5, 'The gioi phu thuy huyen bi qua loi van cua J.K. Rowling.'),
(3, 6, 5, 'Kinh dien cua van hoc thieu nhi the gioi!'),
(4, 6, 5, 'Doc khong biet bao nhieu lan van thay me man.'),
(2, 10, 5, 'Mot tac pham day am anh ve su co don.');
GO
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
