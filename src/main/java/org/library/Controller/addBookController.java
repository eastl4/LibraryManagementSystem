package org.library.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.entity.Book;
import org.library.inventory.BookInventory;
import org.library.service.impl.BookService;

import java.io.IOException;

public class addBookController {
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField quantityField;
    @FXML private TextField availableField;

    private BookInventory inventory;
    private BookService bookService;

    public void initData(BookInventory inventory, BookService bookService) {
        this.inventory = inventory;
        this.bookService = bookService;
    }

    @FXML
    private void handleAddBook() {
        try {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int available = Integer.parseInt(availableField.getText());

            Book newBook = new Book(title, author, isbn, quantity, available);
            inventory.addBook(newBook, bookService);

            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Vui lòng nhập số hợp lệ cho Số lượng và Sẵn có.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) isbnField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Lỗi dữ liệu");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void onClearField() {
        isbnField.clear();
        titleField.clear();
        availableField.clear();
        quantityField.clear();
//        bookTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void onOpenSearchAPI() {
        closeWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/BookSearchOnline.fxml"));
            Parent root = loader.load();
            BookSearchController controller = loader.getController();
            controller.init(inventory, bookService);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("không thể mở của sổ tìm sách bằng API");
        }

    }
}
