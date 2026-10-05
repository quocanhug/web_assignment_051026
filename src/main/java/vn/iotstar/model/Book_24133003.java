package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Book_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookid;
    private Integer isbn;
    private String title;
    private String publisher;
    private BigDecimal price;
    private String description;
    private Date publishDate;
    private String coverImage;
    private Integer quantity;

    // Các trường tiện ích hiển thị
    private List<Author_24133003> authors = new ArrayList<>();
    private String authorNames;
    private int reviewCount;
    private double avgRating;

    public Book_24133003() {
    }

    public Book_24133003(int bookid, Integer isbn, String title, String publisher, BigDecimal price,
                         String description, Date publishDate, String coverImage, Integer quantity) {
        this.bookid = bookid;
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.publishDate = publishDate;
        this.coverImage = coverImage;
        this.quantity = quantity;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Author_24133003> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author_24133003> authors) {
        this.authors = authors;
        if (authors != null && !authors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < authors.size(); i++) {
                sb.append(authors.get(i).getAuthorName());
                if (i < authors.size() - 1) {
                    sb.append(", ");
                }
            }
            this.authorNames = sb.toString();
        }
    }

    public String getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(String authorNames) {
        this.authorNames = authorNames;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
}
