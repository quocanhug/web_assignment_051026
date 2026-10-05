package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IBookDao_24133003;
import vn.iotstar.dao.impl.BookDaoImpl_24133003;
import vn.iotstar.model.Book_24133003;
import vn.iotstar.service.IBookService_24133003;

public class BookServiceImpl_24133003 implements IBookService_24133003 {

    private IBookDao_24133003 bookDao = new BookDaoImpl_24133003();

    @Override
    public List<Book_24133003> findAll(int page, int pageSize) {
        return bookDao.findAll(page, pageSize);
    }

    @Override
    public int countAll() {
        return bookDao.countAll();
    }

    @Override
    public List<Book_24133003> findByAuthor(int authorId, int page, int pageSize) {
        return bookDao.findByAuthor(authorId, page, pageSize);
    }

    @Override
    public int countByAuthor(int authorId) {
        return bookDao.countByAuthor(authorId);
    }

    @Override
    public Book_24133003 findById(int id) {
        return bookDao.findById(id);
    }

    @Override
    public int insert(Book_24133003 book, List<Integer> authorIds) {
        return bookDao.insert(book, authorIds);
    }

    @Override
    public boolean update(Book_24133003 book, List<Integer> authorIds) {
        return bookDao.update(book, authorIds);
    }

    @Override
    public boolean delete(int id) {
        return bookDao.delete(id);
    }
}
