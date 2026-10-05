package vn.iotstar.service;

import java.util.List;
import vn.iotstar.model.Rating_24133003;

public interface IRatingService_24133003 {
    List<Rating_24133003> findByBookId(int bookId);
    int countReviewsByBookId(int bookId);
    Rating_24133003 findByUserAndBook(int userId, int bookId);
    boolean saveOrUpdate(Rating_24133003 rating);
}
