package org.library.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.library.Observer.Observable;
import org.library.Observer.Observer;
import org.library.entity.Book;
import org.library.service.impl.BookService;

import java.util.ArrayList;
import java.util.List;

public class BookInventory implements Observable<ObservableList<Book>> {
    private  ObservableList<Book> bookList = FXCollections.observableArrayList();
    private List<Observer<ObservableList<Book>>> observers = new ArrayList<>();

    public ObservableList<Book> getBookList() {
        return bookList;
    }

    @Override
    public void addObserver(Observer<ObservableList<Book>> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ObservableList<Book>> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<ObservableList<Book>> observer : observers) {
            observer.update(bookList);
        }
    }

    public void loadBooks(BookService bookService) {
        bookList.setAll(bookService.getAllBooks());
        notifyObservers();
    }

    public void addBook(Book book, BookService bookService) {
        bookService.addBook(book);
        loadBooks(bookService);
    }

    public void updateBook(Book book, BookService bookService) {
        bookService.updateBook(book);
        loadBooks(bookService);
    }

    public void deleteBook(Book book, BookService bookService) {
        bookService.deleteBook(book.getISBN());
        loadBooks(bookService);
    }
}
