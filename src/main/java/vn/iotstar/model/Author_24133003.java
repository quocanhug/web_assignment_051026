package vn.iotstar.model;

import java.io.Serializable;
import java.sql.Date;

public class Author_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int authorId;
    private String authorName;
    private Date dateOfBirth;

    public Author_24133003() {
    }

    public Author_24133003(int authorId, String authorName, Date dateOfBirth) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
