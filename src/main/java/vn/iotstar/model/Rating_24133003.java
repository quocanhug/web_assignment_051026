package vn.iotstar.model;

import java.io.Serializable;

public class Rating_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userid;
    private int bookid;
    private Integer rating;
    private String reviewText;

    // Trường hiển thị
    private String userFullName;
    private String userEmail;

    public Rating_24133003() {
    }

    public Rating_24133003(int userid, int bookid, Integer rating, String reviewText) {
        this.userid = userid;
        this.bookid = bookid;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
