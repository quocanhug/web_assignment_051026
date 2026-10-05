package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IRatingDao_24133003;
import vn.iotstar.dao.impl.RatingDaoImpl_24133003;
import vn.iotstar.model.Rating_24133003;
import vn.iotstar.service.IRatingService_24133003;

public class RatingServiceImpl_24133003 implements IRatingService_24133003 {

    private IRatingDao_24133003 ratingDao = new RatingDaoImpl_24133003();

    @Override
    public List<Rating_24133003> findByBookId(int bookId) {
        return ratingDao.findByBookId(bookId);
    }

    @Override
    public int countReviewsByBookId(int bookId) {
        return ratingDao.countReviewsByBookId(bookId);
    }

    @Override
    public Rating_24133003 findByUserAndBook(int userId, int bookId) {
        return ratingDao.findByUserAndBook(userId, bookId);
    }

    @Override
    public boolean saveOrUpdate(Rating_24133003 rating) {
        return ratingDao.saveOrUpdate(rating);
    }
}
