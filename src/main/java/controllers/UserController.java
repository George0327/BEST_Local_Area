package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import model.User;
import model.Volunteer;
import repository.UserDAO;

import java.util.List;

public class UserController {
    @FXML
    private BorderPane mainLayout;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> volunteerColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    private User authenticatedUser;
    private UserDAO userDAO;

    public void initialize(User authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        this.userDAO = new UserDAO();
        userTableView.setEditable(true);

        columnSetup();

        List<User> users = userDAO.getAllUsers();
        userTableView.getItems().addAll(users);
    }

    private void refreshTable() {
        List<User> users = userDAO.getAllUsers();

        // Clear and repopulate the TableView
        userTableView.getItems().clear();
        userTableView.getItems().addAll(users);
    }
    private void columnSetup(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setUsername(event.getNewValue());
            userDAO.updateUser(user);
        });

        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setPassword(event.getNewValue());
            userDAO.updateUser(user);
        });

        volunteerColumn.setCellValueFactory(new PropertyValueFactory<>("volunteerName"));

        // Role column with ComboBox
        ObservableList<String> roles = FXCollections.observableArrayList("admin", "client");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setCellFactory(ComboBoxTableCell.forTableColumn(roles));
        roleColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setRole(event.getNewValue());
            userDAO.updateUser(user);
        });
    }
}
