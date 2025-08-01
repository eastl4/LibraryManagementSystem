package org.library.service.interfaces;

import org.library.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    void addBook(Book book);

    void removeBook(int bookId);

    void updateBook(Book book);

}
