package org.library.ui.Controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.Observer.Observer;
import org.library.entity.Book;
import org.library.entity.Member;
import org.library.inventory.MemberInventory;
import org.library.service.impl.MemberService;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberMenuController implements Initializable, Observer<ObservableList<Member>> {

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, Integer> colMemberId;
    @FXML private TableColumn<Member, String> colFirstName;
    @FXML private TableColumn<Member, String> colLastName;
    @FXML private TableColumn<Member, String> colDob;
    @FXML private TableColumn<Member, String> colAddress;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, String> colContact;

    //  TextFields for Editing/Displaying Details
    @FXML private TextField memberIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField dobField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField contactField;

    @FXML private TextField searchField;

    private MemberInventory inventory;
    private MemberService memberService;
    private FilteredList<Member> filteredMembers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MemberMenuController: Initializing UI components...");
        setupTableColumns(); // Cấu hình cột
        setupRowSelectionListener(); // Listener để hiển thị chi tiết khi chọn dòng
        // Không load data hay đăng ký observer ở đây theo mẫu này
        if(memberIdField != null) {
            memberIdField.setDisable(true); // Không cho sửa ID
        }
        System.out.println("MemberMenuController: UI components initialized.");
    }


    public void init(MemberInventory inventory, MemberService memberService) {
        System.out.println("MemberMenuController: Initializing data and dependencies...");
        if (inventory == null || memberService == null) {
            System.err.println("MemberMenuController Error: Inventory and Service cannot be null in init method.");
            showAlert("Initialization Error","Failed to initialize controller dependencies.", Alert.AlertType.ERROR);
            return;
        }
        this.inventory = inventory;
        this.memberService = memberService;

        this.inventory.addObserver(this);
        System.out.println("MemberMenuController: Registered as observer.");

        this.inventory.loadMembers(this.memberService);
        System.out.println("MemberMenuController: Initial data load requested.");
    }

    //  Cấu hình bảng
    private void setupTableColumns() {
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth")); // Liên kết với thuộc tính String
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
    }

    //  Listener khi chọn dòng
    private void setupRowSelectionListener() {
        memberTable.getSelectionModel().selectedItemProperty().addListener((obs, oldMember, selectedMember) -> {
            if (selectedMember != null) {
                // Hiển thị thông tin của member được chọn lên các TextField
                memberIdField.setText(String.valueOf(selectedMember.getMemberID()));
                firstNameField.setText(selectedMember.getFirstName());
                lastNameField.setText(selectedMember.getLastName());
                dobField.setText(selectedMember.getDateOfBirth());
                addressField.setText(selectedMember.getAddress());
                emailField.setText(selectedMember.getEmail());
                contactField.setText(selectedMember.getContactNumber());
            } else {
                clearFields();
            }
        });
    }

    //  Cài đặt chức năng tìm kiếm
    private void setupSearch() {
        if (searchField == null) return;

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal == null ? "" : newVal.toLowerCase().trim();

            filteredMembers.setPredicate(member -> {
                if (filter.isEmpty()) {
                    return true;
                }

                // Thử phân tích filter thành số nguyên (cho tìm kiếm theo ID)
                try {
                    int id = Integer.parseInt(filter);
                    return member.getMemberID() == id;
                } catch (NumberFormatException e) {
                    // Nếu việc phân tích thất bại, coi đó là tìm kiếm theo tên
                    return (member.getFirstName() != null && member.getFirstName().toLowerCase().contains(filter)) ||
                            (member.getLastName() != null && member.getLastName().toLowerCase().contains(filter)) ||
                            (member.getEmail() != null && member.getEmail().toLowerCase().contains(filter)) ||
                            (member.getContactNumber() != null && member.getContactNumber().contains(filter));
                }
            });
        });
        System.out.println("MemberMenuController: Thiết lập tìm kiếm theo tên và ID.");
    }

    @Override
    public void update(ObservableList<Member> updatedList) {
        filteredMembers = new FilteredList<>(updatedList, b -> true);
        memberTable.setItems(filteredMembers);
        setupSearch();
    }


    // Action Handlers cho Buttons

    @FXML
    private void onAddMember() {
        try {
            // Lấy dữ liệu từ các TextField
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String dob = dobField.getText().trim(); // Lấy String DOB
            String address = addressField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                showAlert("Validation Error", "First Name and Last Name cannot be empty.", Alert.AlertType.WARNING);
                return;
            }

            Member newMember = new Member(firstName, lastName, dob, address, email, contact);

            inventory.addMember(newMember, memberService); // Inventory sẽ load lại và notify

            showAlert("Success", "Member added (or request sent). Table will refresh.", Alert.AlertType.INFORMATION);
            clearFields();

        } catch (Exception e) {
            showAlert("Error", "Failed to add member:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdateMember() {
        // Lấy member đang được chọn
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert("Selection Error", "Please select a member to update.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Lấy dữ liệu đã chỉnh sửa từ các TextField
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String dob = dobField.getText().trim();
            String address = addressField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                showAlert("Validation Error", "First Name and Last Name cannot be empty.", Alert.AlertType.WARNING);
                return;
            }

            Member updatedMember = new Member(selectedMember.getMemberID(), firstName, lastName, dob, address, email, contact);

            inventory.updateMember(updatedMember, memberService); // Inventory sẽ load lại và notify

            showAlert("Success", "Member update request sent. Table will refresh.", Alert.AlertType.INFORMATION);
            clearFields();
            memberTable.getSelectionModel().clearSelection(); // Bỏ chọn dòng hiện tại

        } catch (Exception e) { // Bắt lỗi từ Inventory/Service
            showAlert("Error", "Failed to update member:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDeleteMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert("Selection Error", "Please select a member to delete.", Alert.AlertType.WARNING);
            return;
        }

        // Xác nhận lại lần nữa
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Member: " + selectedMember.getFirstName() + " " + selectedMember.getLastName());
        confirmAlert.setContentText("Are you sure you want to delete this member (ID: " + selectedMember.getMemberID() + ")?");

        ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL); // Sử dụng orElse để cung cấp giá trị mặc định

        if (result == ButtonType.YES) {
            try {
                // Gọi phương thức của Inventory
                inventory.deleteMember(selectedMember.getMemberID(), memberService); // Inventory sẽ load lại và notify
                showAlert("Success", "Member delete request sent. Table will refresh.", Alert.AlertType.INFORMATION);
                clearFields();
                memberTable.getSelectionModel().clearSelection();
            } catch (Exception e) { // Bắt lỗi từ Inventory/Service
                showAlert("Error", "Failed to delete member:\n" + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onClearFields() {
        clearFields();
        memberTable.getSelectionModel().clearSelection(); // Bỏ chọn dòng trong bảng
        searchField.clear(); // Xóa cả ô tìm kiếm
        if (filteredMembers != null) {
            filteredMembers.setPredicate(m -> true); // Reset bộ lọc
            searchField.clear(); // Đảm bảo ô tìm kiếm cũng được xóa
        }
    }

    // --- Helper Methods ---
    private void clearFields() {
        if (memberIdField != null) memberIdField.clear();
        if (firstNameField != null) firstNameField.clear();
        if (lastNameField != null) lastNameField.clear();
        if (dobField != null) dobField.clear();
        if (addressField != null) addressField.clear();
        if (emailField != null) emailField.clear();
        if (contactField != null) contactField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}