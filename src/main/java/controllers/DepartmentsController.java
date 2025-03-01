package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import model.CoreTeam;
import model.Department;
import model.User;
import repository.DepartmentDAO;

import java.io.IOException;
import java.util.List;

public class DepartmentsController {

    private User authenticatedUser;

    private DepartmentDAO departmentDAO;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> idColumn;

    @FXML
    private TableColumn<Department, String> depNameColumn;

    @FXML
    private Button addDepartmentButton;

    @FXML
    private Button deleteDepartmentButton;

    @FXML
    private TextField addDeptTextField;

    public void initialize(User authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        this.departmentDAO = new DepartmentDAO();

        if ("admin".equals(authenticatedUser.getRole())) {
            departmentTableView.setEditable(true);
            deleteDepartmentButton.setDisable(true);

            departmentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                deleteDepartmentButton.setDisable(newValue == null);
            });
        } else if ("client".equals(authenticatedUser.getRole())) {
            departmentTableView.setEditable(false);
            addDepartmentButton.setVisible(false);
            addDeptTextField.setVisible(false);
            deleteDepartmentButton.setVisible(false);
        }

        columnSetup();

        List<Department> departments = departmentDAO.getAllDepartments();
        departmentTableView.getItems().addAll(departments);
    }

    private void columnSetup() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        depNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        depNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        depNameColumn.setOnEditCommit(event -> {
            Department department = event.getRowValue();
            department.setName(event.getNewValue());

            departmentDAO.updateDepartment(department);
            refreshTable();
        });
    }

        @FXML
    private void onViewDetailsButtonClicked(){

    }

    @FXML
    private void onAddDeptButtonClicked(){
        String deptName = addDeptTextField.getText();

        if(deptName.isEmpty()){
            return;
        }

        departmentDAO.addDepartment(deptName);
        refreshTable();
    }

    @FXML
    private void onDeleteDeptButtonClicked(){
        Department selectedDepartment = departmentTableView.getSelectionModel().getSelectedItem();

        if(selectedDepartment != null){
            departmentDAO.deleteDepartment(selectedDepartment.getId());
            refreshTable();
            deleteDepartmentButton.setDisable(true);
        }
    }

    private void refreshTable() {
        // Fetch data from the database
        List<Department> departments = departmentDAO.getAllDepartments();

        // Clear and repopulate the TableView
        departmentTableView.getItems().clear();
        departmentTableView.getItems().addAll(departments);
    }
}
