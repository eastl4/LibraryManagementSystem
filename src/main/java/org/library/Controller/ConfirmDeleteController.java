package org.library.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.library.entity.Book;
import org.library.inventory.BookInventory;
import org.library.service.impl.BookService;

public class ConfirmDeleteController {
    @FXML
    private Label attention;

    private BookService bookService;
    private BookInventory bookInventory;
    private Book bookDelete;

    public void init(Book book, BookInventory bookInventory, BookService bookService) {
        this.bookDelete = book;
        this.bookInventory = bookInventory;
        this.bookService = bookService;

        attention.setText("Bạn muốn xóa sách \"" + book.getTitle() + "\"");
    }

    @FXML
    public void handleDelete() {
        if (bookDelete != null && bookService != null) {
            bookInventory.deleteBook(bookDelete, bookService);
        }
        closeWindow();
    }

    @FXML
    public void closeWindow() {
        ((Stage) attention.getScene().getWindow()).close();
    }
}
