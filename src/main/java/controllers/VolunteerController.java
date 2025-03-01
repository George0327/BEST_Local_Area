package controllers;

import additional.DatePickerCellFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;
import model.Volunteer;
import repository.VolunteerDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class VolunteerController{

    private HomeController homeController;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TableView<Volunteer> volunteerTableView;

    @FXML
    private TableColumn<Volunteer, Integer> idColumn;

    @FXML
    private TableColumn<Volunteer, String> firstnameColumn;

    @FXML
    private TableColumn<Volunteer, String> lastnameColumn;

    @FXML
    private TableColumn<Volunteer, String> emailColumn;

    @FXML
    private TableColumn<Volunteer, String> phoneColumn;

    @FXML
    private TableColumn<Volunteer, LocalDate> birthdayColumn;

    @FXML
    private TableColumn<Volunteer, Boolean> isActiveColumn;

    @FXML
    private Button addVolunteerButton;

    @FXML
    private Button deleteVolunteerButton;

    @FXML
    private Button viewProfileButton;


    private User authenticatedUser;
    private repository.VolunteerDAO volunteerDAO;

    public void initialize(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.volunteerDAO = new VolunteerDAO();

        viewProfileButton.setDisable(true);

        if ("admin".equals(authenticatedUser.getRole())) {
            volunteerTableView.setEditable(true);
            addVolunteerButton.setVisible(true);
            deleteVolunteerButton.setVisible(true);
            deleteVolunteerButton.setDisable(true);

            volunteerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // Enable deleteVolunteerButton when a row is selected
                deleteVolunteerButton.setDisable(newValue == null);
            });
        } else if ("client".equals(authenticatedUser.getRole())) {
            volunteerTableView.setEditable(false);
            addVolunteerButton.setVisible(false);
            deleteVolunteerButton.setVisible(false);
        }

        volunteerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewProfileButton.setDisable(newValue == null);
        });

        // Set up the columns
        columnSetup();

        // Fetch data from the database
        List<Volunteer> volunteers = volunteerDAO.getAllVolunteers();

        // Populate the TableView
        volunteerTableView.getItems().addAll(volunteers);
    }

    @FXML
    private void onAddVolunteerButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/add_volunteer_view.fxml"));
            Parent addVolunteerRoot = loader.load();

            AddVolunteerController addVolunteerController = loader.getController();
            addVolunteerController.setOnVolunteerAddedListener(() -> refreshTable());
            addVolunteerController.setVolunteerDAO(volunteerDAO); // Set the VolunteerDAO

            mainLayout.setRight(addVolunteerRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteVolunteerButtonClicked() {
        Volunteer selectedVolunteer = volunteerTableView.getSelectionModel().getSelectedItem();

        if (selectedVolunteer != null) {
            // Confirmation dialog can be added here if needed
            volunteerDAO.deleteVolunteer(selectedVolunteer.getId());
            refreshTable();
            deleteVolunteerButton.setDisable(true); // Disable the button after deletion
        }
    }

    @FXML
    private void onViewProfileButtonClicked(){
        Volunteer selectedVolunteer = volunteerTableView.getSelectionModel().getSelectedItem();

        if (selectedVolunteer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/profile_view.fxml"));
                Parent profileRoot = loader.load();

                ProfileController profileController = loader.getController();
                profileController.initialize(authenticatedUser, selectedVolunteer);

                Stage stage = new Stage();
                stage.setScene(new Scene(profileRoot));
                stage.setTitle("Volunteer Profile");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshTable() {
        // Fetch data from the database
        List<Volunteer> volunteers = volunteerDAO.getAllVolunteers();

        // Clear and repopulate the TableView
        volunteerTableView.getItems().clear();
        volunteerTableView.getItems().addAll(volunteers);
    }

    private void columnSetup(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        firstnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstnameColumn.setOnEditCommit(event -> {
            Volunteer volunteer = event.getRowValue();
            volunteer.setFirstname(event.getNewValue());

            volunteerDAO.updateVolunteer(volunteer);
        });

        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastnameColumn.setOnEditCommit(event -> {
            Volunteer volunteer = event.getRowValue();
            volunteer.setLastname(event.getNewValue());

            volunteerDAO.updateVolunteer(volunteer);
        });

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> handleEmailEdit(event));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(event -> handlePhoneEdit(event));

        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        if ("admin".equals(authenticatedUser.getRole())) {
            birthdayColumn.setCellFactory(new DatePickerCellFactory<>());
            birthdayColumn.setOnEditCommit(event -> {
                Volunteer volunteer = event.getRowValue();
                volunteer.setBirthday(event.getNewValue());

                volunteerDAO.updateVolunteer(volunteer);
            });
        }

        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        isActiveColumn.setCellFactory(ComboBoxTableCell.forTableColumn(true, false));
        isActiveColumn.setOnEditCommit(event -> {
            Volunteer volunteer = event.getRowValue();
            volunteer.setActive(event.getNewValue());

            volunteerDAO.updateVolunteer(volunteer);
        });
    }
    private void handleEmailEdit(TableColumn.CellEditEvent<Volunteer, String> event) {
        Volunteer volunteer = event.getRowValue();
        String newEmail = event.getNewValue();

        volunteer.setEmail(newEmail);
        volunteerDAO.updateVolunteer(volunteer);
    }

    private void handlePhoneEdit(TableColumn.CellEditEvent<Volunteer, String> event) {
        Volunteer volunteer = event.getRowValue();
        String newPhone = event.getNewValue();

        volunteer.setPhone(newPhone);
        volunteerDAO.updateVolunteer(volunteer); //error handling is done here
    }

}

