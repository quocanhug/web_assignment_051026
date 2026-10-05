package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import vn.iotstar.connection.DBConnection_24133003;
import vn.iotstar.dao.IUserDao_24133003;
import vn.iotstar.model.User_24133003;
import vn.iotstar.util.PasswordUtil_24133003;

public class UserDaoImpl_24133003 implements IUserDao_24133003 {

    private DBConnection_24133003 db = new DBConnection_24133003();

    private User_24133003 mapResultSetToUser(ResultSet rs) throws Exception {
        User_24133003 user = new User_24133003();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setFullname(rs.getString("fullname"));
        user.setPhone((Integer) rs.getObject("phone"));
        user.setPasswd(rs.getString("passwd"));
        user.setSignupDate(rs.getTimestamp("signup_date"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setIsAdmin((Boolean) rs.getObject("is_admin"));
        return user;
    }

    @Override
    public User_24133003 findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User_24133003 findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User_24133003 login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User_24133003 user = mapResultSetToUser(rs);
                    if (!PasswordUtil_24133003.verify(password, user.getPasswd())) return null;
                    if (!user.getPasswd().startsWith("pbkdf2$")) {
                        String hashed = PasswordUtil_24133003.hash(password);
                        try (PreparedStatement update = conn.prepareStatement("UPDATE users SET passwd=? WHERE id=? AND passwd=?")) {
                            update.setString(1, hashed); update.setInt(2, user.getId()); update.setString(3, user.getPasswd());
                            update.executeUpdate();
                        }
                        user.setPasswd(hashed);
                    }
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean register(User_24133003 user) {
        String sql = "INSERT INTO users (email, fullname, phone, passwd, signup_date, is_admin) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullname());
            if (user.getPhone() != null) ps.setInt(3, user.getPhone()); else ps.setNull(3, java.sql.Types.INTEGER);
            ps.setString(4, PasswordUtil_24133003.hash(user.getPasswd()));
            ps.setTimestamp(5, user.getSignupDate() != null ? user.getSignupDate() : new Timestamp(System.currentTimeMillis()));
            ps.setBoolean(6, user.getIsAdmin() != null ? user.getIsAdmin() : false);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkExistEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
