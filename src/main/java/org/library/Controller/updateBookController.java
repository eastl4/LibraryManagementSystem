package org.library.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.entity.Book;
import org.library.inventory.BookInventory;
import org.library.service.impl.BookService;

public class updateBookController {

    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField quantityField;
    @FXML private TextField availableField;

    private BookInventory inventory;
    private BookService bookService;
    private Book bookToUpdate;

    public void init(Book book,BookInventory inventory, BookService bookService) {
        this.bookToUpdate = book;
        this.bookService = bookService;
        this.inventory = inventory;

        titleField.setText(book.getTitle());
        isbnField.setText(book.getISBN());
        authorField.setText(book.getAuthor());
        availableField.setText(String.valueOf(book.getAvailable()));
        quantityField.setText(String.valueOf(book.getQuantity()));

    }

    @FXML
    private void handleUpdateBook() {
        try {
            bookToUpdate.setTitle(titleField.getText());
            bookToUpdate.setISBN(isbnField.getText());
            bookToUpdate.setAuthor(authorField.getText());
            bookToUpdate.setQuantity(Integer.parseInt(quantityField.getText()));
            bookToUpdate.setAvailable(Integer.parseInt(availableField.getText()));

            inventory.updateBook(bookToUpdate, bookService);

            ((Stage) titleField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            // Show alert nếu cần
            showAlert("Điền đúng số lượng và sẵn có là số nguyên");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Lỗi dữ liệu");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void onCancel() {
        ((Stage) titleField.getScene().getWindow()).close();
    }

}
