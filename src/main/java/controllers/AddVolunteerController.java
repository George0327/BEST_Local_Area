package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repository.VolunteerDAO;
import model.Volunteer;
import additional.OnVolunteerAddedListener;

import java.io.IOException;
import java.time.LocalDate;

public class AddVolunteerController {
    private VolunteerDAO volunteerDAO;

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
    private Button okButton;

    @FXML
    private Button cancelButton;


    private OnVolunteerAddedListener onVolunteerAddedListener;

    public void setOnVolunteerAddedListener(OnVolunteerAddedListener listener) {
        this.onVolunteerAddedListener = listener;
    }

    public VolunteerDAO getVolunteerDAO() {
        return volunteerDAO;
    }

    public void setVolunteerDAO(VolunteerDAO volunteerDAO) {
        this.volunteerDAO = volunteerDAO;
    }

    @FXML
    private void onOkButtonClicked() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText();
        LocalDate birthday = birthdayDatePicker.getValue();

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || birthday == null){
            openErrorView("Invalid input", "All the fields must be completed");
            return;
        }

        Volunteer newVolunteer = new Volunteer(firstName, lastName, email, phone, birthday);

        volunteerDAO.addVolunteer(newVolunteer);

        if (onVolunteerAddedListener != null) {
            Platform.runLater(() -> onVolunteerAddedListener.onVolunteerAdded());
        }

        BorderPane mainBorderPane = (BorderPane) okButton.getParent().getParent().getParent();
        mainBorderPane.setRight(null);
    }

    @FXML
    private void onCancelButtonClicked() {
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
