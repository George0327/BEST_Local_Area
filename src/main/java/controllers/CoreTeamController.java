package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.User;
import repository.CoreTeamDAO;
import model.CoreTeam;

import java.io.IOException;
import java.util.List;

public class CoreTeamController {

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TableView<CoreTeam> coreteamTableView;

    @FXML
    private TableColumn<CoreTeam, Integer> idColumn;

    @FXML
    private TableColumn<CoreTeam, String> teamnameColumn;

    @FXML
    private TableColumn<CoreTeam, String> mainorganiserColumn;

    @FXML
    private TableColumn<CoreTeam, Integer> leaderIdColumn;

    @FXML
    private TableColumn<CoreTeam, String> projectColumn;

    @FXML
    private Button addCoreTeamButton;

    @FXML
    private Button deleteCoreTeamButton;

    @FXML
    private TextField addCoreTeamTextField;

    private User authenticatedUser;
    private repository.CoreTeamDAO coreTeamDAO;

    public void initialize(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.coreTeamDAO = new CoreTeamDAO();

        if ("admin".equals(authenticatedUser.getRole())) {
            coreteamTableView.setEditable(true);
            addCoreTeamButton.setVisible(true);
            deleteCoreTeamButton.setVisible(true);
            deleteCoreTeamButton.setDisable(true);

            coreteamTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                deleteCoreTeamButton.setDisable(newValue == null);
            });
        } else if ("client".equals(authenticatedUser.getRole())) {
            coreteamTableView.setEditable(false);
            addCoreTeamButton.setVisible(false);
            deleteCoreTeamButton.setVisible(false);
            addCoreTeamTextField.setVisible(false);
        }

        columnSetup();

        List<CoreTeam> coreTeams = coreTeamDAO.getAllCoreTeams();
        coreteamTableView.getItems().addAll(coreTeams);
    }

    private void columnSetup() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        teamnameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamnameColumn.setOnEditCommit(event -> {
            CoreTeam coreTeam = event.getRowValue();
            coreTeam.setTeamName(event.getNewValue());

            coreTeamDAO.updateCoreTeam(coreTeam);
            refreshTable();
        });

        leaderIdColumn.setCellValueFactory(new PropertyValueFactory<>("leaderId"));
        leaderIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        leaderIdColumn.setOnEditCommit(event -> {
            CoreTeam coreTeam = event.getRowValue();
            try {
                coreTeam.setLeaderId(Integer.parseInt(event.getNewValue().toString()));
                coreTeamDAO.updateCoreTeam(coreTeam);
                refreshTable();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        mainorganiserColumn.setCellValueFactory(new PropertyValueFactory<>("mainOrganiser"));

        projectColumn.setCellValueFactory(new PropertyValueFactory<>("project"));

    }

    @FXML
    private void onAddCoreTeamButtonClicked() {
        String teamName = addCoreTeamTextField.getText();

        if(teamName.isEmpty()){
            return;
        }

        coreTeamDAO.addCoreTeam(teamName);
        refreshTable();
    }

    @FXML
    private void onDeleteCoreTeamButtonClicked() {
        CoreTeam selectedCoreTeam = coreteamTableView.getSelectionModel().getSelectedItem();

        if (selectedCoreTeam != null) {
            coreTeamDAO.deleteCoreTeam(selectedCoreTeam.getId());
            refreshTable();
            deleteCoreTeamButton.setDisable(true);
        }
    }

    private void refreshTable() {
        // Fetch data from the database
        List<CoreTeam> coreTeams = coreTeamDAO.getAllCoreTeams();

        // Clear and repopulate the TableView
        coreteamTableView.getItems().clear();
        coreteamTableView.getItems().addAll(coreTeams);
    }

    @FXML
    private void onViewDetailsButtonClicked() {
        CoreTeam selectedCoreTeam = coreteamTableView.getSelectionModel().getSelectedItem();

        if (selectedCoreTeam != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/best_local_area/ctDetails_view.fxml"));
                Parent detailsRoot = loader.load();

                CtDetailsController ctDetailsController = loader.getController();
                ctDetailsController.initialize(authenticatedUser, selectedCoreTeam);

                Stage stage = new Stage();
                stage.setScene(new Scene(detailsRoot));
                stage.setTitle("Core Team details");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
