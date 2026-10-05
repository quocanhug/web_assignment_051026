package vn.iotstar.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String email;
    private String fullname;
    private Integer phone;
    private String passwd;
    private Timestamp signupDate;
    private Timestamp lastLogin;
    private Boolean isAdmin;

    public User_24133003() {
    }

    public User_24133003(int id, String email, String fullname, Integer phone, String passwd,
                         Timestamp signupDate, Timestamp lastLogin, Boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.passwd = passwd;
        this.signupDate = signupDate;
        this.lastLogin = lastLogin;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Timestamp getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Timestamp signupDate) {
        this.signupDate = signupDate;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getIsAdmin() {
        return isAdmin != null ? isAdmin : false;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return Boolean.TRUE.equals(this.isAdmin);
    }
}
