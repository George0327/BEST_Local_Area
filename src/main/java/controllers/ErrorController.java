package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button cancelButton;

    public void setErrorTitle(String errorTitle) {
        titleLabel.setText(errorTitle);
    }

    public void setErrorDescription(String errorDescription) {
        descriptionLabel.setText(errorDescription);
    }

    @FXML
    private void onCancelButtonClick() {
        closeCurrentStage();
    }

    private void closeCurrentStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
