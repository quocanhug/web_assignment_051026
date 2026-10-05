package vn.iotstar.service.impl;

import vn.iotstar.dao.IUserDao_24133003;
import vn.iotstar.dao.impl.UserDaoImpl_24133003;
import vn.iotstar.model.User_24133003;
import vn.iotstar.service.IUserService_24133003;

public class UserServiceImpl_24133003 implements IUserService_24133003 {

    private IUserDao_24133003 userDao = new UserDaoImpl_24133003();

    @Override
    public User_24133003 findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User_24133003 findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User_24133003 login(String email, String password) {
        User_24133003 user = userDao.login(email, password);
        if (user != null) {
            userDao.updateLastLogin(user.getId());
        }
        return user;
    }

    @Override
    public boolean register(User_24133003 user) {
        return userDao.register(user);
    }

    @Override
    public boolean updateLastLogin(int userId) {
        return userDao.updateLastLogin(userId);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }
}
