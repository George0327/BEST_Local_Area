package controllers;

import model.User;
import repository.LoginService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User authenticatedUser = LoginService.authenticateUser(username, password);

        //System.out.println("Authenticated User id: " + authenticatedUser.getId());

        if (authenticatedUser != null) {
            // Successful login
            openHomeView(authenticatedUser);
            closeCurrentStage();
        } else {
            // Invalid credentials, display an error message or take appropriate action
            openErrorView("Invalid credentials", "Your username or password are incorrect. Please try again");
        }
    }

    private void openHomeView(User authenticatedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/example/best_local_area/home_view.fxml"));
            Parent root = loader.load();

            // Access the controller of the next view
            HomeController homeController = loader.getController();

            // Pass the authenticated user to the next view controller
            homeController.initialize(authenticatedUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Next View");

            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void openErrorView(String errorTitle, String errorDescription) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/example/best_local_area/error_view.fxml"));
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
