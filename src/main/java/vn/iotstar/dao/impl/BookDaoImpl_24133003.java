package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.connection.DBConnection_24133003;
import vn.iotstar.model.Author_24133003;
import vn.iotstar.dao.IBookDao_24133003;
import vn.iotstar.model.Book_24133003;

public class BookDaoImpl_24133003 implements IBookDao_24133003 {

    private DBConnection_24133003 db = new DBConnection_24133003();

    private Book_24133003 mapResultSetToBook(ResultSet rs, Connection conn) throws Exception {
        Book_24133003 book = new Book_24133003();
        int bookId = rs.getInt("bookid");
        book.setBookid(bookId);
        book.setIsbn((Integer) rs.getObject("isbn"));
        book.setTitle(rs.getString("title"));
        book.setPublisher(rs.getString("publisher"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setDescription(rs.getString("description"));
        book.setPublishDate(rs.getDate("publish_date"));
        book.setCoverImage(rs.getString("cover_image"));
        book.setQuantity((Integer) rs.getObject("quantity"));

        // Lấy danh sách tác giả
        List<Author_24133003> authors = new ArrayList<>();
        try (PreparedStatement psAuthors = conn.prepareStatement("SELECT a.* FROM author a INNER JOIN book_author ba ON a.author_id=ba.author_id WHERE ba.bookid=?")) {
            psAuthors.setInt(1, bookId);
            try (ResultSet authorRows = psAuthors.executeQuery()) {
                while (authorRows.next()) authors.add(new Author_24133003(authorRows.getInt("author_id"),
                        authorRows.getString("author_name"), authorRows.getDate("date_of_birth")));
            }
        }
        book.setAuthors(authors);

        // Lấy số lượng review từ bảng rating
        String sqlReview = "SELECT COUNT(*), AVG(CAST(rating AS FLOAT)) FROM rating WHERE bookid = ?";
        try (PreparedStatement psReview = conn.prepareStatement(sqlReview)) {
            psReview.setInt(1, bookId);
            try (ResultSet rsReview = psReview.executeQuery()) {
                if (rsReview.next()) {
                    book.setReviewCount(rsReview.getInt(1));
                    book.setAvgRating(rsReview.getDouble(2));
                }
            }
        }
        return book;
    }

    @Override
    public List<Book_24133003> findAll(int page, int pageSize) {
        List<Book_24133003> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM books ORDER BY bookid DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBook(rs, conn));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM books";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Book_24133003> findByAuthor(int authorId, int page, int pageSize) {
        List<Book_24133003> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.* FROM books b "
                   + "INNER JOIN book_author ba ON b.bookid = ba.bookid "
                   + "WHERE ba.author_id = ? "
                   + "ORDER BY b.bookid DESC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBook(rs, conn));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countByAuthor(int authorId) {
        String sql = "SELECT COUNT(*) FROM books b "
                   + "INNER JOIN book_author ba ON b.bookid = ba.bookid "
                   + "WHERE ba.author_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Book_24133003 findById(int id) {
        String sql = "SELECT * FROM books WHERE bookid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs, conn);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tải thông tin sách.", e);
        }
        return null;
    }

    @Override
    public int insert(Book_24133003 book, List<Integer> authorIds) {
        String sql = "INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (book.getIsbn() != null) ps.setInt(1, book.getIsbn()); else ps.setNull(1, java.sql.Types.INTEGER);
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getPublisher());
            ps.setBigDecimal(4, book.getPrice());
            ps.setString(5, book.getDescription());
            ps.setDate(6, book.getPublishDate());
            ps.setString(7, book.getCoverImage());
            if (book.getQuantity() != null) ps.setInt(8, book.getQuantity()); else ps.setNull(8, java.sql.Types.INTEGER);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        int generatedId = rsKeys.getInt(1);
                        if (authorIds != null && !authorIds.isEmpty()) {
                            String sqlAuthor = "INSERT INTO book_author (bookid, author_id) VALUES (?, ?)";
                            try (PreparedStatement psAuthor = conn.prepareStatement(sqlAuthor)) {
                                for (Integer aId : authorIds) {
                                    psAuthor.setInt(1, generatedId);
                                    psAuthor.setInt(2, aId);
                                    psAuthor.addBatch();
                                }
                                psAuthor.executeBatch();
                            }
                        }
                        return generatedId;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean update(Book_24133003 book, List<Integer> authorIds) {
        String sql = "UPDATE books SET isbn = ?, title = ?, publisher = ?, price = ?, description = ?, "
                   + "publish_date = ?, cover_image = ?, quantity = ? WHERE bookid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (book.getIsbn() != null) ps.setInt(1, book.getIsbn()); else ps.setNull(1, java.sql.Types.INTEGER);
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getPublisher());
            ps.setBigDecimal(4, book.getPrice());
            ps.setString(5, book.getDescription());
            ps.setDate(6, book.getPublishDate());
            ps.setString(7, book.getCoverImage());
            if (book.getQuantity() != null) ps.setInt(8, book.getQuantity()); else ps.setNull(8, java.sql.Types.INTEGER);
            ps.setInt(9, book.getBookid());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                if (authorIds != null) {
                    String sqlDel = "DELETE FROM book_author WHERE bookid = ?";
                    try (PreparedStatement psDel = conn.prepareStatement(sqlDel)) {
                        psDel.setInt(1, book.getBookid());
                        psDel.executeUpdate();
                    }
                    if (!authorIds.isEmpty()) {
                        String sqlAuthor = "INSERT INTO book_author (bookid, author_id) VALUES (?, ?)";
                        try (PreparedStatement psAuthor = conn.prepareStatement(sqlAuthor)) {
                            for (Integer aId : authorIds) {
                                psAuthor.setInt(1, book.getBookid());
                                psAuthor.setInt(2, aId);
                                psAuthor.addBatch();
                            }
                            psAuthor.executeBatch();
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement psR = conn.prepareStatement("DELETE FROM rating WHERE bookid = ?")) {
                psR.setInt(1, id);
                psR.executeUpdate();
            }
            try (PreparedStatement psBA = conn.prepareStatement("DELETE FROM book_author WHERE bookid = ?")) {
                psBA.setInt(1, id);
                psBA.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE bookid = ?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
