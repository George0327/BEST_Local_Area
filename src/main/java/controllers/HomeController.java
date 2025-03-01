package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import model.Volunteer;
import repository.UserDAO;

import java.io.IOException;

public class HomeController {
    private User authenticatedUser;
    private UserDAO userDAO;
    private Volunteer volunteer;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private Button signOutButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button volunteersButton;

    @FXML
    private Button editUsersButton;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label roleLabel;


    @FXML
    public void initialize(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.userDAO = new UserDAO();
        mainLayout.setUserData(this);

        if ("admin".equals(authenticatedUser.getRole())) {
            editUsersButton.setVisible(true);
        } else if ("client".equals(authenticatedUser.getRole())){
            editUsersButton.setVisible(false);
        }

        this.volunteer = userDAO.getVolunteerByUser(authenticatedUser);

        firstNameLabel.setText(volunteer.getFirstname());
        lastNameLabel.setText(volunteer.getLastname());
        roleLabel.setText(authenticatedUser.getRole());
    }

    @FXML
    private void onExitButtonClick() {
        Platform.exit();
    }

    @FXML
    private void onSignOutButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/login_view.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("BEST Local Area");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) signOutButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onProfileButtonClick(){
        viewProfile(authenticatedUser, volunteer);
    }

    public void viewProfile(User authenticatedUser, Volunteer volunteer){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/profile_view.fxml"));
            Parent profileRoot = loader.load();

            ProfileController profileController = loader.getController();
            profileController.initialize(authenticatedUser, volunteer);

            mainLayout.setCenter(profileRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onVolunteersButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/volunteers_view.fxml"));
            Parent volunteerRoot = loader.load();

            // Access the controller of the Volunteer view to pass the authenticated user
            VolunteerController volunteerController = loader.getController();
            volunteerController.initialize(authenticatedUser);

            // Set the volunteer view as the center content of the mainLayout
            mainLayout.setCenter(volunteerRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCoreTeamsButtonClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/core_team_view.fxml"));
            Parent coreTeamRoot = loader.load();

            CoreTeamController coreTeamController = loader.getController();
            coreTeamController.initialize(authenticatedUser);

            mainLayout.setCenter(coreTeamRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDepartmentsButtonClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/departments_view.fxml"));
            Parent deptRoot = loader.load();

            DepartmentsController departmentsController = loader.getController();
            departmentsController.initialize(authenticatedUser);

            mainLayout.setCenter(deptRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onProjectsButtonClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/project_view.fxml"));
            Parent projectRoot = loader.load();

            ProjectController projectController = loader.getController();
            projectController.initialize(authenticatedUser);

            mainLayout.setCenter(projectRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCompaniesButtonClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/company_view.fxml"));
            Parent companyRoot = loader.load();

            CompanyController companyController = loader.getController();
            companyController.initialize(authenticatedUser);

            mainLayout.setCenter(companyRoot);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditUsersButtonClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/users_view.fxml"));
            Parent userRoot = loader.load();

            UserController userController = loader.getController();
            userController.initialize(authenticatedUser);

            mainLayout.setCenter(userRoot);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
