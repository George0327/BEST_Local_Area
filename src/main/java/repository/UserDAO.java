package repository;

import model.User;
import model.Volunteer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT u.id, u.username, u.password, v.firstname, u.role " +
                     "FROM users u " +
                     "LEFT JOIN volunteers v ON u.volunteer_id = v.id")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String volunteerName = resultSet.getString("firstname");
                String role = resultSet.getString("role");

                User user = new User(id, username, password, volunteerName, role);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void updateUser(User user) {
        String updateUserQuery = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery)) {

            updateUserStatement.setString(1, user.getUsername());
            updateUserStatement.setString(2, user.getPassword());
            updateUserStatement.setString(3, user.getRole());
            updateUserStatement.setInt(4, user.getId());

            int affectedRows = updateUserStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows updated. Check if the ID exists.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Volunteer getVolunteerByUser(User user) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT v.* FROM volunteers v " +
                    "INNER JOIN users u ON v.id = u.volunteer_id " +
                    "WHERE u.id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Extract volunteer details from the result set
                        int volunteerId = resultSet.getInt("id");
                        String firstName = resultSet.getString("firstname");
                        String lastName = resultSet.getString("lastname");
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone");
                        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                        boolean active = resultSet.getBoolean("isactive");

                        // Create a Volunteer object
                        return new Volunteer(volunteerId, firstName, lastName, email, phone, birthday, active);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
        }

        return null;
    }
}

