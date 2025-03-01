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
import javafx.util.converter.IntegerStringConverter;
import model.CoreTeam;
import model.Project;
import model.User;
import repository.ProfileDAO;
import repository.ProjectDAO;

import java.io.IOException;
import java.util.List;

public class ProjectController {

    private User authenticatedUser;

    private ProjectDAO projectDAO;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private TableColumn<Project, Integer> idColumn;

    @FXML
    private TableColumn<Project, String> projectNameColumn;

    @FXML
    private TableColumn<Project, Integer> ctIdColumn;

    @FXML
    private TableColumn<Project, String> coreTeamColumn;

    @FXML
    private Button addProjectButton;

    @FXML
    private Button deleteProjectButton;

    @FXML
    private TextField addProjectTextField;

    public void initialize(User authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        this.projectDAO = new ProjectDAO();

        if ("admin".equals(authenticatedUser.getRole())) {
            projectTableView.setEditable(true);
            deleteProjectButton.setDisable(true);

            projectTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                deleteProjectButton.setDisable(newValue == null);
            });
        } else if ("client".equals(authenticatedUser.getRole())) {
            projectTableView.setEditable(false);
            addProjectButton.setVisible(false);
            addProjectTextField.setVisible(false);
            deleteProjectButton.setVisible(false);
        }

        columnSetup();

        List<Project> projects = projectDAO.getAllProjectsWithCoreTeamNames();
        projectTableView.getItems().addAll(projects);

    }

    private void columnSetup() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        projectNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectNameColumn.setOnEditCommit(event -> {
            Project project = event.getRowValue();
            project.setProjectName(event.getNewValue());

            projectDAO.updateProject(project);
            refreshTable();
        });

        ctIdColumn.setCellValueFactory(new PropertyValueFactory<>("ctId"));
        ctIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        ctIdColumn.setOnEditCommit(event -> {
            Project project = event.getRowValue();
            try {
                project.setCtId(Integer.parseInt(event.getNewValue().toString()));
                projectDAO.updateProject(project);
                refreshTable();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        coreTeamColumn.setCellValueFactory(new PropertyValueFactory<>("ctName"));
    }

    @FXML
    private void onViewDetailsButtonClicked() {}

    @FXML
    private void onAddProjectButtonClicked(){
        String projectName = addProjectTextField.getText();

        if(projectName.isEmpty()){
            return;
        }

        projectDAO.addProject(projectName);
        refreshTable();
    }

    @FXML
    private void onDeleteProjectButtonClicked(){
        Project selectedProject = projectTableView.getSelectionModel().getSelectedItem();

        if(selectedProject != null){
            projectDAO.deleteProject(selectedProject.getId());
            refreshTable();
            deleteProjectButton.setDisable(true);
        }
    }

    private void refreshTable() {
        // Fetch data from the database
        List<Project> projects = projectDAO.getAllProjectsWithCoreTeamNames();

        // Clear and repopulate the TableView
        projectTableView.getItems().clear();
        projectTableView.getItems().addAll(projects);
    }
}
