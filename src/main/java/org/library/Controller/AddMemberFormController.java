package org.library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.library.entity.Member;
import org.library.service.impl.MemberService;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
public class AddMemberFormController {

    @FXML
    private Text formTitle; // Thêm fx:id="formTitle" vào Text tiêu đề trong FXML

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField dateOfBirthField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactNumberField;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;

    private MemberService memberService;
    private Member memberToEdit;

    @FXML
    public void initialize() {
        firstNameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                lastNameField.requestFocus();
                event.consume();
            }
        });
        lastNameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                dateOfBirthField.requestFocus();
                event.consume();
            }
        });
        dateOfBirthField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addressField.requestFocus();
                event.consume();
            }
        });
        addressField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                emailField.requestFocus();
                event.consume();
            }
        });
        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                contactNumberField.requestFocus();
                event.consume();
            }
        });
        contactNumberField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSaveButton(new ActionEvent(event.getSource(), event.getTarget()));
                event.consume();
            }
        });

    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    // Phương thức để nhận đối tượng Member cần chỉnh sửa (Chỉ dùng cho chế độ Update)
    public void setMemberToEdit(Member member) {
        this.memberToEdit = member;
        if (memberToEdit != null) {
            formTitle.setText("Cập nhật Thông tin Người Mượn"); // Đổi tiêu đề form
            firstNameField.setText(memberToEdit.getFirstName());
            lastNameField.setText(memberToEdit.getLastName());
            dateOfBirthField.setText(memberToEdit.getDateOfBirth());
            addressField.setText(memberToEdit.getAddress());
            emailField.setText(memberToEdit.getEmail());
            contactNumberField.setText(memberToEdit.getContactNumber());
        } else {
            formTitle.setText("Thêm Người Mượn Mới");
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dateOfBirth = dateOfBirthField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String contactNumber = contactNumberField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || address.isEmpty() || email.isEmpty() || contactNumber.isEmpty()) {
            statusLabel.setText("Vui lòng nhập đầy đủ thông tin.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        if (memberService != null) {
            try {
                boolean success = false;
                String actionMessage = "";

                if (memberToEdit == null) { // Chế độ Thêm mới
                    Member newMember = new Member(); // Tạo đối tượng mới
                    newMember.setFirstName(firstName);
                    newMember.setLastName(lastName);
                    newMember.setDateOfBirth(dateOfBirth);
                    newMember.setAddress(address);
                    newMember.setEmail(email);
                    newMember.setContactNumber(contactNumber);

                    success = memberService.addMember(newMember);
                    actionMessage = "Thêm người mượn";

                } else { // Chế độ Cập nhật
                    // Sử dụng đối tượng memberToEdit đã được truyền vào
                    memberToEdit.setFirstName(firstName);
                    memberToEdit.setLastName(lastName);
                    memberToEdit.setDateOfBirth(dateOfBirth);
                    memberToEdit.setAddress(address);
                    memberToEdit.setEmail(email);
                    memberToEdit.setContactNumber(contactNumber);

                    success = memberService.updateMember(memberToEdit); // Gọi phương thức cập nhật
                    actionMessage = "Cập nhật người mượn";
                }

                if (success) {
                    statusLabel.setText(actionMessage + " thành công!");
                    statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);

                    closeStage(event);

                } else {
                    statusLabel.setText("Lỗi khi " + actionMessage + ".");
                    statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Đã xảy ra lỗi hệ thống " + ": " + e.getMessage());
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            }

        } else {
            statusLabel.setText("Lỗi: MemberService chưa được thiết lập.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            System.err.println("MemberService is null in AddMemberFormController.");
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        closeStage(event);
    }

    // Phương thức tiện ích để đóng Stage hiện tại
    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();
    }
}