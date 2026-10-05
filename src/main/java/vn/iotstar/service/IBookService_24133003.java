package vn.iotstar.service;

import java.util.List;
import vn.iotstar.model.Book_24133003;

public interface IBookService_24133003 {
    List<Book_24133003> findAll(int page, int pageSize);
    int countAll();

    List<Book_24133003> findByAuthor(int authorId, int page, int pageSize);
    int countByAuthor(int authorId);

    Book_24133003 findById(int id);
    int insert(Book_24133003 book, List<Integer> authorIds);
    boolean update(Book_24133003 book, List<Integer> authorIds);
    boolean delete(int id);
}
