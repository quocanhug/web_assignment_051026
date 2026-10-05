package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.connection.DBConnection_24133003;
import vn.iotstar.dao.IRatingDao_24133003;
import vn.iotstar.model.Rating_24133003;

public class RatingDaoImpl_24133003 implements IRatingDao_24133003 {

    private DBConnection_24133003 db = new DBConnection_24133003();

    @Override
    public List<Rating_24133003> findByBookId(int bookId) {
        List<Rating_24133003> list = new ArrayList<>();
        String sql = "SELECT r.userid, r.bookid, r.rating, r.review_text, u.fullname, u.email "
                   + "FROM rating r "
                   + "INNER JOIN users u ON r.userid = u.id "
                   + "WHERE r.bookid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rating_24133003 r = new Rating_24133003();
                    r.setUserid(rs.getInt("userid"));
                    r.setBookid(rs.getInt("bookid"));
                    int ratingVal = rs.getInt("rating");
                    if (!rs.wasNull()) {
                        r.setRating(ratingVal);
                    }
                    r.setReviewText(rs.getString("review_text"));
                    r.setUserFullName(rs.getString("fullname"));
                    r.setUserEmail(rs.getString("email"));
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countReviewsByBookId(int bookId) {
        String sql = "SELECT COUNT(*) FROM rating WHERE bookid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
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
    public Rating_24133003 findByUserAndBook(int userId, int bookId) {
        String sql = "SELECT r.userid, r.bookid, r.rating, r.review_text, u.fullname, u.email "
                   + "FROM rating r "
                   + "INNER JOIN users u ON r.userid = u.id "
                   + "WHERE r.userid = ? AND r.bookid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rating_24133003 r = new Rating_24133003();
                    r.setUserid(rs.getInt("userid"));
                    r.setBookid(rs.getInt("bookid"));
                    int ratingVal = rs.getInt("rating");
                    if (!rs.wasNull()) {
                        r.setRating(ratingVal);
                    }
                    r.setReviewText(rs.getString("review_text"));
                    r.setUserFullName(rs.getString("fullname"));
                    r.setUserEmail(rs.getString("email"));
                    return r;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(Rating_24133003 rating) {
        Rating_24133003 existing = findByUserAndBook(rating.getUserid(), rating.getBookid());
        if (existing != null) {
            String sql = "UPDATE rating SET rating = ?, review_text = ? WHERE userid = ? AND bookid = ?";
            try (Connection conn = db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                if (rating.getRating() != null) ps.setInt(1, rating.getRating()); else ps.setNull(1, java.sql.Types.TINYINT);
                ps.setString(2, rating.getReviewText());
                ps.setInt(3, rating.getUserid());
                ps.setInt(4, rating.getBookid());
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String sql = "INSERT INTO rating (userid, bookid, rating, review_text) VALUES (?, ?, ?, ?)";
            try (Connection conn = db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, rating.getUserid());
                ps.setInt(2, rating.getBookid());
                if (rating.getRating() != null) ps.setInt(3, rating.getRating()); else ps.setNull(3, java.sql.Types.TINYINT);
                ps.setString(4, rating.getReviewText());
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
