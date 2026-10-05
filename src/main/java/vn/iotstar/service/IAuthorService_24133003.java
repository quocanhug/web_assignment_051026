package vn.iotstar.service;

import java.util.List;
import vn.iotstar.model.Author_24133003;

public interface IAuthorService_24133003 {
    List<Author_24133003> findAll();
    Author_24133003 findById(int id);
    List<Author_24133003> findByBookId(int bookId);
    Author_24133003 findByName(String name);
    List<Integer> getOrCreateAuthorIds(String authorNamesStr);
    void insert(Author_24133003 author);
}
