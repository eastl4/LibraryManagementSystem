
package org.library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text actiontarget;

    @FXML
    public void initialize() {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLoginButton(new ActionEvent(event.getSource(), event.getTarget()));
            }
        });

        usernameField.requestFocus();
    }
    @FXML
    protected void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "admin".equals(password)) {
            actiontarget.setText("Đăng nhập thành công!");
            actiontarget.setFill(javafx.scene.paint.Color.GREEN);

            // Chuyển sang màn hình Menu chính
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/MainMenu.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Ứng dụng Quản lý Thư viện - Menu Chính");
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                actiontarget.setText("Lỗi khi tải màn hình Menu chính.");
                actiontarget.setFill(javafx.scene.paint.Color.RED);
            }

        } else {
            actiontarget.setText("Sai tên đăng nhập hoặc mật khẩu.");
            actiontarget.setFill(javafx.scene.paint.Color.RED);
        }
    }
}
