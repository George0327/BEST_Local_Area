package repository;

import controllers.ErrorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Volunteer;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;
    // Retrieve all volunteers from the database
    public List<Volunteer> getAllVolunteers() {
        List<Volunteer> volunteers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM volunteers");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                boolean isActive = resultSet.getBoolean("isactive");

                Volunteer volunteer = new Volunteer(id, firstname, lastname, email, phone, birthday, isActive);
                volunteers.add(volunteer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return volunteers;
    }

    // Update volunteer in the database
    public void updateVolunteer(Volunteer volunteer) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("UPDATE volunteers SET firstname = ?, lastname = ?, email = ?, phone = ?, birthday = ?, isactive = ? WHERE id = ?")) {

            statement.setString(1, volunteer.getFirstname());
            statement.setString(2, volunteer.getLastname());
            statement.setString(3, volunteer.getEmail());
            statement.setString(4, volunteer.getPhone());
            statement.setDate(5, java.sql.Date.valueOf(volunteer.getBirthday()));
            statement.setBoolean(6, volunteer.isActive());
            statement.setInt(7, volunteer.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            if (e instanceof PSQLException && ((PSQLException) e).getSQLState().equals("23505")) {

                openErrorView("Not unique email or phone number", "Please input an unique email and phone number");
            } else {
                // Other SQL exception, handle it based on your application's error handling strategy
                e.printStackTrace();
            }
        }
    }

    public void addVolunteer(Volunteer volunteer) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String volunteersSql = "INSERT INTO volunteers (firstname, lastname, email, phone, birthday) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement volunteersStatement = connection.prepareStatement(volunteersSql, Statement.RETURN_GENERATED_KEYS)) {
                volunteersStatement.setString(1, volunteer.getFirstname());
                volunteersStatement.setString(2, volunteer.getLastname());
                volunteersStatement.setString(3, volunteer.getEmail());
                volunteersStatement.setString(4, volunteer.getPhone());
                volunteersStatement.setObject(5, volunteer.getBirthday());

                volunteersStatement.executeUpdate();

                // Retrieve the generated volunteer ID
                ResultSet generatedKeys = volunteersStatement.getGeneratedKeys();
                int volunteerId = -1;
                if (generatedKeys.next()) {
                    volunteerId = generatedKeys.getInt(1);
                }

                // Insert into users table
                String usersSql = "INSERT INTO users (username, password, volunteer_id, role) VALUES (?, ?, ?, ?)";
                try (PreparedStatement usersStatement = connection.prepareStatement(usersSql)) {
                    usersStatement.setString(1, volunteer.getFirstname()); // Assuming firstname is used as the username
                    usersStatement.setString(2, volunteer.getLastname()); // Assuming lastname is used as the password
                    usersStatement.setInt(3, volunteerId);
                    usersStatement.setString(4, "client");

                    usersStatement.executeUpdate();
                }
            } catch (SQLException e) {
                // Check if the exception is due to a unique constraint violation
                if (e instanceof PSQLException && ((PSQLException) e).getSQLState().equals("23505")) {

                    openErrorView("Not unique email or phone number", "Please input an unique email and phone number");
                } else {
                    // Other SQL exception, handle it based on your application's error handling strategy
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteVolunteer(int volunteerId) {
        if (isLeaderOfCoreTeam(volunteerId)) {
            openErrorView("Cannot Delete Volunteer", "The volunteer is the leader of a core team. Please reassign the team leader before deleting.");
            return;
        }

        String deleteVolunteerSql = "DELETE FROM volunteers WHERE id = ?;";
        String deleteUserSql = "DELETE FROM users WHERE volunteer_id = ?;";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement deleteVolunteerStatement = connection.prepareStatement(deleteVolunteerSql);
             PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserSql)) {

            // Set parameters for the deleteVolunteerStatement
            deleteVolunteerStatement.setInt(1, volunteerId);
            // Execute the deleteVolunteerStatement
            deleteVolunteerStatement.executeUpdate();

            // Set parameters for the deleteUserStatement
            deleteUserStatement.setInt(1, volunteerId);
            // Execute the deleteUserStatement
            deleteUserStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isLeaderOfCoreTeam(int volunteerId) {
        String checkLeaderSql = "SELECT COUNT(*) FROM coreteams WHERE leaderid = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement checkLeaderStatement = connection.prepareStatement(checkLeaderSql)) {

            // Set parameters for the checkLeaderStatement
            checkLeaderStatement.setInt(1, volunteerId);

            // Execute the checkLeaderStatement
            ResultSet resultSet = checkLeaderStatement.executeQuery();

            // Check if the volunteer is the leader of any core team
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private void openErrorView(String errorTitle, String errorDescription) {
        try {
            FXMLLoader loader = new FXMLLoader(VolunteerDAO.class.getResource("/com/example/best_local_area/error_view.fxml"));
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
