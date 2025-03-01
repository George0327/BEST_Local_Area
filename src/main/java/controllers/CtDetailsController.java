package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CoreTeam;
import model.User;
import model.Volunteer;
import repository.CoreTeamDAO;

import java.util.List;

public class CtDetailsController {

    private CoreTeamDAO coreTeamDAO;
    private CoreTeam coreTeam;

    @FXML
    private TableView<Volunteer> membersTableView;

    @FXML
    private TableColumn<Volunteer, String> membersColumn;

    @FXML
    private Label teamNameLabel;

    @FXML
    private TextField idTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    public void initialize(User authenticatedUser, CoreTeam coreTeam){
        this.coreTeamDAO = new CoreTeamDAO();
        this.coreTeam = coreTeam;

        membersColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        List<Volunteer> members = coreTeamDAO.getVolunteersInCoreTeam(coreTeam.getId());
        membersTableView.getItems().addAll(members);

        teamNameLabel.setText(coreTeam.getTeamName());

        if ("client".equals(authenticatedUser.getRole())) {
            idTextField.setVisible(false);
            addButton.setVisible(false);
            removeButton.setVisible(false);
        }
    }

    @FXML
    private void onAddButtonClick(){
        String id = idTextField.getText();

        if(!id.isEmpty()) {
            int volId = Integer.parseInt(id);
            coreTeamDAO.addVolunteerToCoreTeam(volId, coreTeam.getId());
        }
        refreshTable();
    }

    @FXML
    private void onRemoveButtonClick(){
        Volunteer selectedVolunteer = membersTableView.getSelectionModel().getSelectedItem();

        if(selectedVolunteer != null){
            coreTeamDAO.deleteVolunteerFromCoreTeam(coreTeam.getId(), selectedVolunteer.getId());
            refreshTable();
        }
    }

    private void refreshTable() {
        // Fetch data from the database
        List<Volunteer> members = coreTeamDAO.getVolunteersInCoreTeam(coreTeam.getId());

        // Clear and repopulate the TableView
        membersTableView.getItems().clear();
        membersTableView.getItems().addAll(members);
    }
}
