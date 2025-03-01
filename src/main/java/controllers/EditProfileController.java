package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import model.Volunteer;
import repository.ProfileDAO;
import repository.UserDAO;
import repository.VolunteerDAO;

import java.io.IOException;
import java.time.LocalDate;

public class EditProfileController {
    private ProfileDAO profileDAO;

    private VolunteerDAO volunteerDAO;

    private UserDAO userDAO;

    private User associatedUser;

    private Volunteer volunteer;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    public void initialize(User authenticatedUser, Volunteer volunteer){
        this.profileDAO = new ProfileDAO();
        this.userDAO = new UserDAO();
        this.volunteerDAO = new VolunteerDAO();
        this.volunteer = volunteer;
        this.associatedUser = profileDAO.getUserByVolunteer(volunteer);

        firstNameTextField.setText(volunteer.getFirstname());
        lastNameTextField.setText(volunteer.getLastname());
        birthdayDatePicker.setValue(volunteer.getBirthday());
        emailTextField.setText(volunteer.getEmail());
        phoneTextField.setText(volunteer.getPhone());

        usernameTextField.setText(associatedUser.getUsername());
        passwordTextField.setText(associatedUser.getPassword());
    }

    @FXML
    private void onOkButtonClicked(){
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate birthday = birthdayDatePicker.getValue();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText();

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || birthday == null || username.isEmpty() || password.isEmpty()){
            openErrorView("Invalid input", "All the fields must be completed");
            return;
        }

        Volunteer tempVolunteer = new Volunteer(volunteer.getId(), firstName, lastName, email, phone, birthday, volunteer.isActive());

        volunteerDAO.updateVolunteer(tempVolunteer);

        User tempUser = new User(associatedUser.getId(), username, password, associatedUser.getRole());

        userDAO.updateUser(tempUser);

        BorderPane mainBorderPane = (BorderPane) okButton.getParent().getParent().getParent();
        mainBorderPane.setRight(null);
    }

    @FXML
    private void onCancelButtonClicked(){
        BorderPane mainBorderPane = (BorderPane) cancelButton.getParent().getParent().getParent();
        mainBorderPane.setRight(null);
    }

    private void openErrorView(String errorTitle, String errorDescription) {
        try {
            FXMLLoader loader = new FXMLLoader(AddVolunteerController.class.getResource("/com/example/best_local_area/error_view.fxml"));
            Parent root = loader.load();

            // Access the controller of the error view
            ErrorController errorController = loader.getController();

            // Pass the error message to the error view controller
            errorController.setErrorTitle(errorTitle);
            errorController.setErrorDescription(errorDescription);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Error");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
