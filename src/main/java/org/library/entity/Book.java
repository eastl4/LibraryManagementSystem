package org.library.entity;

public class Book {

    //Basic indetification.
    private String title;
    private String author;
    private String ISBN;
    private int quantity;

    //Status information.
    private int available;

    public Book() {
    }

    public Book(Book otherBook) {
        this.ISBN = otherBook.ISBN;
        this.title = otherBook.title;
        this.author = otherBook.author;
        this.quantity = otherBook.quantity;
        this.available = otherBook.available;
    }

    public Book(String title, String author, String ISBN, int quantity, int available) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.quantity = quantity;
        this.available = available;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int availableCopies) {
        this.available = availableCopies;
    }
}
