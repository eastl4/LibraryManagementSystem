package org.library.service.impl;

import javafx.collections.ObservableList;
import org.library.dao.impl.BookDAOImpl;
import org.library.entity.Book;
import org.library.factory.DAOFactory;

public class BookService {
    private BookDAOImpl bookDAO;

    public BookService(BookDAOImpl bookDAO) {
        this.bookDAO = bookDAO;
    }

    public boolean addBook(Book book) {
        try {
            boolean result = bookDAO.save(book);
            if (result) {
                DAOFactory.commitTransaction();
            } else {
                DAOFactory.rollbackTransaction();
            }
            return result;
        } catch (Exception e) {
            DAOFactory.rollbackTransaction();
            throw e;
        }
    }

    public boolean updateBook(Book book) {
        try {
            boolean result = bookDAO.update(book);
            if (result) {
                DAOFactory.commitTransaction();
            } else {
                DAOFactory.rollbackTransaction();
            }
            return result;
        } catch (Exception e) {
            DAOFactory.rollbackTransaction();
            throw e;
        }
    }

    public boolean deleteBook(String isbn) {
        try {
            boolean result = bookDAO.delete(isbn);
            if (result) {
                DAOFactory.commitTransaction();
            } else {
                DAOFactory.rollbackTransaction();
            }
            return result;
        } catch (Exception e) {
            DAOFactory.rollbackTransaction();
            throw e;
        }
    }

    public ObservableList<Book> getAllBooks() {
        return bookDAO.findAll();
    }
}
