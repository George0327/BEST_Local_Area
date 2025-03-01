package repository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controllers.ErrorController;
import controllers.VolunteerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CoreTeam;
import model.Volunteer;

public class CoreTeamDAO {
    // Replace these constants with your actual database connection details
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;

    public List<CoreTeam> getAllCoreTeams() {
        List<CoreTeam> coreTeams = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT c.coreteamid, c.teamname, c.leaderid, v.firstname, p.projectname " +
                     "FROM coreteams c " +
                     "LEFT JOIN volunteers v ON c.leaderid = v.id " +
                     "LEFT JOIN projects p ON c.coreteamid = p.coreteamid")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("coreteamid");
                String teamname = resultSet.getString("teamname");
                int leaderId = resultSet.getInt("leaderid");
                String mainOrganiser = resultSet.getString("firstname");
                String projectName = resultSet.getString("projectname");

                CoreTeam coreTeam = new CoreTeam(id, teamname, leaderId, mainOrganiser, projectName);
                coreTeams.add(coreTeam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coreTeams;
    }

    public void updateCoreTeam(CoreTeam coreTeam) {
        String updateCoreTeamQuery = "UPDATE coreteams SET teamname = ?, leaderid = ? WHERE coreteamid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement updateCoreTeamStatement = connection.prepareStatement(updateCoreTeamQuery)) {

            connection.setAutoCommit(false);  // Disable auto-commit to manage the transaction

            updateCoreTeamStatement.setString(1, coreTeam.getTeamName());
            updateCoreTeamStatement.setInt(2, coreTeam.getLeaderId());
            updateCoreTeamStatement.setInt(3, coreTeam.getId());

            int affectedRows = updateCoreTeamStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows updated. Check if the ID exists.");
                return;  // No need to proceed with VolunteersCoreTeams update if core team update failed
            }

            // Now update VolunteersCoreTeams for the new leader
            String insertVolunteersCoreTeamsQuery = "INSERT INTO volunteerscoreteams (volunteerid, coreteamid) VALUES (?, ?)";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertVolunteersCoreTeamsQuery)) {
                // Assuming coreTeam.getLeaderId() is the ID of the volunteer to be added to VolunteersCoreTeams
                insertStatement.setInt(1, coreTeam.getLeaderId());
                insertStatement.setInt(2, coreTeam.getId());

                // Execute the insert
                int result = insertStatement.executeUpdate();

                // Commit the transaction
                connection.commit();

                // Check for success in the insert
                if (result <= 0) {
                    System.out.println("Failed to insert the leader into VolunteersCoreTeams.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();  // Rollback the transaction in case of an exception
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCoreTeam(String teamName) {
        String addCoreTeamQuery = "INSERT INTO coreteams (teamname) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement addCoreTeamStatement = connection.prepareStatement(addCoreTeamQuery, Statement.RETURN_GENERATED_KEYS)) {

            addCoreTeamStatement.setString(1, teamName);

            addCoreTeamStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCoreTeam(int coreTeamId) {
        String deleteCoreTeamSql = "DELETE FROM coreteams WHERE coreteamid = ?;";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement deleteCoreTeamStatement = connection.prepareStatement(deleteCoreTeamSql)) {

            deleteCoreTeamStatement.setInt(1, coreTeamId);
            deleteCoreTeamStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Volunteer> getVolunteersInCoreTeam(int coreTeamId) {
        List<Volunteer> volunteers = new ArrayList<>();

        String query = "SELECT v.id, v.firstname, v.lastname " +
                "FROM volunteers v " +
                "JOIN volunteerscoreteams vc ON v.id = vc.volunteerid " +
                "WHERE vc.coreteamid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, coreTeamId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("firstname");
                    String lastName = resultSet.getString("lastname");
                    // Add other fields as needed

                    Volunteer volunteer = new Volunteer(id, firstName, lastName);
                    volunteers.add(volunteer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return volunteers;
    }

    public void addVolunteerToCoreTeam(int volunteerId, int coreTeamId) {
        // Check if the volunteer ID is valid
        if (!isVolunteerIdValid(volunteerId)) {
            openErrorView("Invalid volunteer id", "Please provide a valid volunteer id");
            return;
        }

        String insertVolunteersCoreTeamsQuery = "INSERT INTO volunteerscoreteams (volunteerid, coreteamid) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement insertStatement = connection.prepareStatement(insertVolunteersCoreTeamsQuery)) {

            insertStatement.setInt(1, volunteerId);
            insertStatement.setInt(2, coreTeamId);

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Volunteer added to the core team successfully.");
            } else {
                System.out.println("Failed to add volunteer to the core team. Check if the IDs are valid.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isVolunteerIdValid(int volunteerId) {
        String checkVolunteerQuery = "SELECT COUNT(*) FROM volunteers WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement checkStatement = connection.prepareStatement(checkVolunteerQuery)) {

            checkStatement.setInt(1, volunteerId);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deleteVolunteerFromCoreTeam(int coreTeamId, int volunteerId) {
        String deleteVolunteerFromCoreTeamQuery = "DELETE FROM volunteerscoreteams WHERE coreteamid = ? AND volunteerid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteVolunteerFromCoreTeamQuery)) {

            deleteStatement.setInt(1, coreTeamId);
            deleteStatement.setInt(2, volunteerId);

            int affectedRows = deleteStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows deleted. Check if the Core Team ID or Volunteer ID is valid.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void openErrorView(String errorTitle, String errorDescription) {
        try {
            FXMLLoader loader = new FXMLLoader(VolunteerController.class.getResource("/com/example/best_local_area/error_view.fxml"));
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
