package org.library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import org.library.Observer.MemberInventoryObserver;
import org.library.dao.impl.IssuedBookDAOImpl;
import org.library.db.DatabaseConnection;
import org.library.entity.Book;
import org.library.entity.IssuedBook;
import org.library.entity.Member;
import org.library.inventory.BookInventory;
import org.library.inventory.MemberInventory;
import org.library.service.impl.BookService;
import org.library.service.impl.MemberService;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.util.Callback;
import javafx.beans.binding.Bindings;
import java.io.IOException;
import java.util.List;

public class MemberMenuController implements MemberInventoryObserver {

    @FXML
    private TableView<Member> memberTable;
    @FXML
    private TableColumn<Member, Integer> memberIDColumn;
    @FXML
    private TableColumn<Member, String> firstNameColumn;
    @FXML
    private TableColumn<Member, String> lastNameColumn;
    @FXML
    private TableColumn<Member, String> dateOfBirthColumn;
    @FXML
    private TableColumn<Member, String> addressColumn;
    @FXML
    private TableColumn<Member, String> emailColumn;
    @FXML
    private TableColumn<Member, String> contactNumberColumn;

    @FXML private TableView<IssuedBook> borrowedBookTable;
    @FXML private TableColumn<IssuedBook, Integer> bookISBNColumn;
    @FXML private TableColumn<IssuedBook, String> bookIDColumn;
    @FXML private TableColumn<IssuedBook, String> issuedDateColumn;
    @FXML private TableColumn<IssuedBook, String> dueDateColumn;

    @FXML
    private TextField searchField;

    private MemberInventory memberInventory;
    private MemberService memberService;
    private FilteredList<Member> filteredMembers;
    private IssuedBookDAOImpl issuedBookDAO = new IssuedBookDAOImpl(DatabaseConnection.getInstance().getConnection());
    private Member selectedMemberForAction;


    public void initialize() {
        setupTable();
        setupRowSelectionListener();
        setupSearch();
        setupContextMenu();
        setupClickToDeselect();
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("issuedBookID"));
        bookISBNColumn.setCellValueFactory(new PropertyValueFactory<>("bookISBN"));
        issuedDateColumn.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

    public void init(MemberInventory memberInventory, MemberService memberService) {
        this.memberInventory = memberInventory;
        this.memberService = memberService;

        memberInventory.addObserver(this);
        memberInventory.loadMembers(memberService);
    }

    private void setupTable() {
        memberIDColumn.setCellValueFactory(new PropertyValueFactory<Member, Integer>("memberID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("lastName"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("dateOfBirth"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("email"));
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("contactNumber"));
    }

    private void setupRowSelectionListener() {
        memberTable.getSelectionModel().selectedItemProperty().addListener((obs, oldMember, selectedMember) -> {
            selectedMemberForAction = selectedMember;
            if (selectedMember != null) {
                int memberID = selectedMember.getMemberID();
                List<IssuedBook> books = issuedBookDAO.borrowedBooks(memberID);
                borrowedBookTable.setItems(FXCollections.observableArrayList(books));
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();

            filteredMembers.setPredicate(member -> {
                if (member == null) return false;

                boolean matchesNumber = false;
                try {
                    int numericFilter = Integer.parseInt(filter);
                    matchesNumber = (member.getMemberID() == numericFilter);
                } catch (NumberFormatException ignored) {
                }

                boolean matchesText = (member.getFirstName() != null && member.getFirstName().toLowerCase().contains(filter)) ||
                        (member.getLastName() != null && member.getLastName().toLowerCase().contains(filter)) ||
                        (member.getAddress() != null && member.getAddress().toLowerCase().contains(filter)) ||
                        (member.getEmail() != null && member.getEmail().toLowerCase().contains(filter)) ||
                        (member.getContactNumber() != null && member.getContactNumber().toLowerCase().contains(filter));

                return matchesText || matchesNumber;
            });
        });
    }
    private void setupClickToDeselect() {
        memberTable.setOnMouseClicked(event -> {
            Node target = event.getPickResult().getIntersectedNode();
            boolean clickedOnPopulatedRow = false;
            while (target != null && !(target instanceof TableRow)) {
                target = target.getParent();
            }
            if (target instanceof TableRow) {
                TableRow clickedRow = (TableRow) target;
                if (!clickedRow.isEmpty()) {
                    clickedOnPopulatedRow = true;
                }
            }
            if (!clickedOnPopulatedRow) {
                memberTable.getSelectionModel().clearSelection();
                selectedMemberForAction = null;
            }
        });
    }

    @Override
    public void update(ObservableList<Member> updatedList) {
        filteredMembers = new FilteredList<>(updatedList, b -> true);
        memberTable.setItems(filteredMembers);
        memberTable.getSelectionModel().clearSelection();
        selectedMemberForAction = null;
    }

    @FXML
    private void onAddMember(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/AddMemberForm.fxml"));
            Parent root = loader.load();

            AddMemberFormController addMemberFormController = loader.getController();
            if (addMemberFormController != null) {
                addMemberFormController.setMemberService(memberService);
            }

            Stage stage = new Stage();
            stage.setTitle("Thêm Người Mượn Mới");
            Scene scene = new Scene(root);
            stage.setScene(scene);
//            scene.getStylesheets().add(getClass().getResource("/org/library/style.css").toExternalForm());
            Stage primaryStage = (Stage) memberTable.getScene().getWindow();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();

            refreshMemberList();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Không thể mở form thêm người mượn.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi hệ thống khi mở form thêm người mượn.");
        }
    }

    @FXML
    private void onUpdateMember(ActionEvent event) {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            showAlert("Vui lòng chọn một người mượn để cập nhật.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/AddMemberForm.fxml")); // Tái sử dụng form
            Parent root = loader.load();

            AddMemberFormController addMemberFormController = loader.getController();
            if (addMemberFormController != null) {
                addMemberFormController.setMemberService(memberService);
                addMemberFormController.setMemberToEdit(selectedMember);
            }

            Stage stage = new Stage();
            stage.setTitle("Cập nhật Thông tin Người Mượn");
            Scene scene = new Scene(root);
            stage.setScene(scene);
//            scene.getStylesheets().add(getClass().getResource("/org/library/style.css").toExternalForm());

            Stage primaryStage = (Stage) memberTable.getScene().getWindow();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();

            refreshMemberList();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Không thể mở form cập nhật người mượn.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi hệ thống khi mở form cập nhật người mượn.");
        }
    }


    @FXML
    private void onDeleteMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            showAlert("Vui lòng chọn một người mượn để xóa.");
            return;
        }
        try {
            boolean wasDeleted = memberInventory.deleteMember(selectedMember.getMemberID(), memberService);

            if (wasDeleted) {
                refreshMemberList();
                showAlert("Xóa người mượn thành công!");
            }
        } catch (Exception e) {
            showAlert("Lỗi khi xóa người mượn: " + e.getMessage());
        }
    }
    @FXML
    private void onBack() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) memberTable.getScene().getWindow();

            Scene sceneBack = new Scene(root);
            stage.setScene(sceneBack);
            stage.setTitle("Ứng dụng Quản lý Thư viện - Menu Chính");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Không thể quay lại trang trước.");
        }
    }
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // Tạo MenuItem cho Thêm thành viên
        MenuItem addMenuItem = new MenuItem("Thêm thành viên");
        addMenuItem.setOnAction(event ->{
            onAddMember(new ActionEvent(event.getSource(), event.getTarget()));
        } );

        // Tạo MenuItem cho Cập nhật
        MenuItem updateMenuItem = new MenuItem("Cập nhật...");
        updateMenuItem.setOnAction(event -> {
            // Gọi phương thức onUpdateMember khi MenuItem này được chọn
            // Truyền một ActionEvent giả vì onUpdateMember() cần nó.
            onUpdateMember(new ActionEvent(event.getSource(), event.getTarget()));
        });

        // Tạo MenuItem cho Xóa
        MenuItem deleteMenuItem = new MenuItem("Xóa");
        deleteMenuItem.setOnAction(event -> {
            // Gọi phương thức onDeleteMember khi MenuItem này được chọn
            onDeleteMember();
        });

        // Thêm các MenuItem vào ContextMenu
        contextMenu.getItems().addAll(addMenuItem, updateMenuItem, deleteMenuItem);
        memberTable.setRowFactory(new Callback<TableView<Member>, TableRow<Member>>() {
            @Override
            public TableRow<Member> call(TableView<Member> tableView) {
                final TableRow<Member> row = new TableRow<>();
                row.contextMenuProperty().bind(
                        // Sử dụng Bindings.when để kiểm tra dòng có rỗng không
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null) // Nếu rỗng, không có ContextMenu (hiển thị null)
                                .otherwise(contextMenu) // Nếu không rỗng, gán contextMenu đã tạo
                );
                return row;
            }
        });
    }

    private void refreshMemberList() {
        if (memberInventory != null && memberService != null) {
            memberInventory.loadMembers(memberService);
        }

    }

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
