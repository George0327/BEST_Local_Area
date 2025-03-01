package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.CoreTeam;
import model.Department;
import model.User;
import model.Volunteer;
import repository.ProfileDAO;

import java.io.IOException;
import java.util.List;

public class ProfileController {

    private User authenticatedUser;
    private Volunteer volunteer;
    private ProfileDAO profileDAO;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TableView<CoreTeam> coreTeamsTable;

    @FXML
    private TableColumn<CoreTeam, String> coreTeamsColumn;

    @FXML
    private TableView<Department> departmentsTable;

    @FXML
    private TableColumn<Department, String> departmentsColumn;

    @FXML
    private Label addPhotoLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Button editProfileButton;


    public void initialize(User authenticatedUser,Volunteer volunteer){
        this.authenticatedUser = authenticatedUser;
        this.volunteer = volunteer;
        this.profileDAO = new ProfileDAO();
        User associatedUser = profileDAO.getUserByVolunteer(volunteer);

        firstNameLabel.setText(volunteer.getFirstname());
        lastNameLabel.setText(volunteer.getLastname());
        birthdayLabel.setText(volunteer.getBirthday().toString());
        emailLabel.setText(volunteer.getEmail());
        phoneLabel.setText(volunteer.getPhone());

        if(volunteer.isActive()){
            statusLabel.setText("active");
        }
        else {
            statusLabel.setText("inactive");
        }

        roleLabel.setText(associatedUser.getRole());

        setupTables(volunteer);

        if ("admin".equals(authenticatedUser.getRole()) || authenticatedUser.getId() == associatedUser.getId()) {
            usernameLabel.setText(associatedUser.getUsername());
            passwordLabel.setText(associatedUser.getPassword());
        } else{
            addPhotoLabel.setVisible(false);
            editProfileButton.setVisible(false);
        }
    }

    @FXML
    private void onEditProfileButtonClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/editProfile_view.fxml"));
            Parent editRoot = loader.load();

            EditProfileController editProfileController = loader.getController();
            editProfileController.initialize(authenticatedUser, volunteer);

            mainLayout.setRight(editRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupTables(Volunteer volunteer){
        ProfileDAO profileDAO = new ProfileDAO();
        List<CoreTeam> coreTeams = profileDAO.getCoreTeamsByVolunteerId(volunteer.getId());

        coreTeamsTable.getItems().addAll(coreTeams);
        coreTeamsColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        List<Department> departments = profileDAO.getDepartmentsByVolunteerId(volunteer.getId());

        departmentsTable.getItems().addAll(departments);
        departmentsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    }
}
