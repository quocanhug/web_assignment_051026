package vn.iotstar.model;

import java.io.Serializable;

public class BookAuthor_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookid;
    private int authorId;

    public BookAuthor_24133003() {
    }

    public BookAuthor_24133003(int bookid, int authorId) {
        this.bookid = bookid;
        this.authorId = authorId;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
