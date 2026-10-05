package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IAuthorDao_24133003;
import vn.iotstar.dao.impl.AuthorDaoImpl_24133003;
import vn.iotstar.model.Author_24133003;
import vn.iotstar.service.IAuthorService_24133003;

public class AuthorServiceImpl_24133003 implements IAuthorService_24133003 {

    private IAuthorDao_24133003 authorDao = new AuthorDaoImpl_24133003();

    @Override
    public List<Author_24133003> findAll() {
        return authorDao.findAll();
    }

    @Override
    public Author_24133003 findById(int id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author_24133003> findByBookId(int bookId) {
        return authorDao.findByBookId(bookId);
    }

    @Override
    public Author_24133003 findByName(String name) {
        return authorDao.findByName(name);
    }

    @Override
    public List<Integer> getOrCreateAuthorIds(String authorNamesStr) {
        List<Integer> ids = new java.util.ArrayList<>();
        if (authorNamesStr == null || authorNamesStr.trim().isEmpty()) {
            return ids;
        }
        String[] names = authorNamesStr.split("[,;]");
        for (String rawName : names) {
            String name = rawName.trim();
            if (name.isEmpty()) continue;
            Author_24133003 existing = authorDao.findByName(name);
            if (existing != null) {
                if (!ids.contains(existing.getAuthorId())) {
                    ids.add(existing.getAuthorId());
                }
            } else {
                Author_24133003 newAuthor = new Author_24133003();
                newAuthor.setAuthorName(name);
                int newId = authorDao.insertReturnId(newAuthor);
                if (newId > 0 && !ids.contains(newId)) {
                    ids.add(newId);
                }
            }
        }
        return ids;
    }

    @Override
    public void insert(Author_24133003 author) {
        authorDao.insert(author);
    }
}
