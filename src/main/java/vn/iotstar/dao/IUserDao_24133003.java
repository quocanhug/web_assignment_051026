package vn.iotstar.dao;

import vn.iotstar.model.User_24133003;

public interface IUserDao_24133003 {
    User_24133003 findByEmail(String email);
    User_24133003 findById(int id);
    User_24133003 login(String email, String password);
    boolean register(User_24133003 user);
    boolean updateLastLogin(int userId);
    boolean checkExistEmail(String email);
}
