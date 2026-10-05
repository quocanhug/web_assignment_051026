package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.connection.DBConnection_24133003;
import vn.iotstar.dao.IAuthorDao_24133003;
import vn.iotstar.model.Author_24133003;

public class AuthorDaoImpl_24133003 implements IAuthorDao_24133003 {

    private DBConnection_24133003 db = new DBConnection_24133003();

    @Override
    public List<Author_24133003> findAll() {
        List<Author_24133003> list = new ArrayList<>();
        String sql = "SELECT author_id, author_name, date_of_birth FROM author ORDER BY author_name ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Author_24133003 author = new Author_24133003();
                author.setAuthorId(rs.getInt("author_id"));
                author.setAuthorName(rs.getString("author_name"));
                author.setDateOfBirth(rs.getDate("date_of_birth"));
                list.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Author_24133003 findById(int id) {
        String sql = "SELECT author_id, author_name, date_of_birth FROM author WHERE author_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Author_24133003 author = new Author_24133003();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setAuthorName(rs.getString("author_name"));
                    author.setDateOfBirth(rs.getDate("date_of_birth"));
                    return author;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Author_24133003> findByBookId(int bookId) {
        List<Author_24133003> list = new ArrayList<>();
        String sql = "SELECT a.author_id, a.author_name, a.date_of_birth "
                   + "FROM author a INNER JOIN book_author ba ON a.author_id = ba.author_id "
                   + "WHERE ba.bookid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Author_24133003 author = new Author_24133003();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setAuthorName(rs.getString("author_name"));
                    author.setDateOfBirth(rs.getDate("date_of_birth"));
                    list.add(author);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Author_24133003 findByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String sql = "SELECT author_id, author_name, date_of_birth FROM author WHERE LOWER(LTRIM(RTRIM(author_name))) = LOWER(LTRIM(RTRIM(?)))";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Author_24133003 author = new Author_24133003();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setAuthorName(rs.getString("author_name"));
                    author.setDateOfBirth(rs.getDate("date_of_birth"));
                    return author;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insertReturnId(Author_24133003 author) {
        String sql = "INSERT INTO author (author_name, date_of_birth) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author.getAuthorName());
            ps.setDate(2, author.getDateOfBirth());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
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
    public void insert(Author_24133003 author) {
        insertReturnId(author);
    }
}
