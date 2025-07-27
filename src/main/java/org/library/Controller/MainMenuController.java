package org.library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.library.factory.DAOFactory;
import org.library.inventory.BookInventory;
import org.library.inventory.IssuedBookInventory;
import org.library.inventory.MemberInventory;
import org.library.service.impl.BookService;
import org.library.service.impl.IssuedBookService;
import org.library.service.impl.MemberService;

import java.io.IOException;

public class MainMenuController {

    //switch scene

    private final BookInventory bookInventory = new BookInventory();
    private final BookService bookService = new BookService(DAOFactory.getBookDAO());

    @FXML
    public void switchToBooks(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/BookMenu.fxml"));
            Parent root = loader.load();

            BookMenuController controller = loader.getController();
            controller.init(bookInventory, bookService);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final MemberInventory memberInventory = new MemberInventory();
    private final MemberService memberService = new MemberService(DAOFactory.getMemberDAO());

    @FXML
    public void switchToMembers(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/MemberMenu.fxml"));
            Parent root = loader.load();

            MemberMenuController controller = loader.getController();
            controller.init(memberInventory, memberService);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final IssuedBookInventory issuedBookInventory = new IssuedBookInventory();
    private final IssuedBookService issuedBookService = new IssuedBookService(DAOFactory.getIssuedBookDAO());

    @FXML
    public void switchToIssueBook(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/IssuedBookMenu.fxml"));
            Parent root = loader.load();

            // Retrieve the controller and initialize it with dependencies
            IssuedBookMenuController controller = loader.getController();
            controller.init(issuedBookInventory, issuedBookService);

            // Switch to the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}