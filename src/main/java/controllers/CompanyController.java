package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import model.Company;
import model.Department;
import model.User;
import repository.CompanyDAO;
import repository.DepartmentDAO;

import java.util.List;

public class CompanyController {
    private User authenticatedUser;

    private CompanyDAO companyDAO;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TableView<Company> companyTableView;

    @FXML
    private TableColumn<Company, Integer> idColumn;

    @FXML
    private TableColumn<Company, String> companyNameColumn;

    @FXML
    private Button addCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private TextField addCompanyTextField;

    public void initialize(User authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        this.companyDAO = new CompanyDAO();

        if ("admin".equals(authenticatedUser.getRole())) {
            companyTableView.setEditable(true);
            deleteCompanyButton.setDisable(true);

            companyTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                deleteCompanyButton.setDisable(newValue == null);
            });
        } else if ("client".equals(authenticatedUser.getRole())) {
            companyTableView.setEditable(false);
            addCompanyButton.setVisible(false);
            addCompanyTextField.setVisible(false);
            deleteCompanyButton.setVisible(false);
        }

        columnSetup();

        List<Company> companies = companyDAO.getAllCompanies();
        companyTableView.getItems().addAll(companies);
    }

    private void columnSetup() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        companyNameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        companyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        companyNameColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setCompanyName(event.getNewValue());

            companyDAO.updateCompany(company);
            refreshTable();
        });
    }

    @FXML
    private void onViewDetailsButtonClicked(){

    }

    @FXML
    private void onAddCompanyButtonClicked(){
        String companyName = addCompanyTextField.getText();

        if(companyName.isEmpty()){
            return;
        }

        companyDAO.addCompany(companyName);
        refreshTable();
    }

    @FXML
    private void onDeleteCompanyButtonClicked(){
        Company selectedCompany = companyTableView.getSelectionModel().getSelectedItem();

        if(selectedCompany != null){
            companyDAO.deleteCompany(selectedCompany.getId());
            refreshTable();
            deleteCompanyButton.setDisable(true);
        }
    }

    private void refreshTable() {
        // Fetch data from the database
        List<Company> companies = companyDAO.getAllCompanies();

        // Clear and repopulate the TableView
        companyTableView.getItems().clear();
        companyTableView.getItems().addAll(companies);
    }
}
