package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.model.Author_24133003;

public interface IAuthorDao_24133003 {
    List<Author_24133003> findAll();
    Author_24133003 findById(int id);
    List<Author_24133003> findByBookId(int bookId);
    Author_24133003 findByName(String name);
    int insertReturnId(Author_24133003 author);
    void insert(Author_24133003 author);
}
