package repository;

import model.User;

import java.sql.*;

public class LoginService {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;
    public static User authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    ) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Extract user details from the result set
                        int id = resultSet.getInt("id");
                        String role = resultSet.getString("role");
                        // You may want to extract other user details as well

                        // Create and return a User object
                        return new User(id, username, password, 0, role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
        }
        return null; // Return null if authentication fails
    }
}

