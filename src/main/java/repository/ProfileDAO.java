package repository;

import model.CoreTeam;
import model.Department;
import model.User;
import model.Volunteer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileDAO {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;

    public List<CoreTeam> getCoreTeamsByVolunteerId(int volunteerId) {
        List<CoreTeam> coreTeams = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT ct.coreteamid, ct.teamname FROM coreteams ct " +
                    "JOIN volunteerscoreteams vct ON ct.coreteamid = vct.coreteamid " +
                    "WHERE vct.volunteerid = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, volunteerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int coreTeamId = resultSet.getInt("coreteamid");
                        String teamName = resultSet.getString("teamname");

                        CoreTeam coreTeam = new CoreTeam(coreTeamId, teamName);
                        coreTeams.add(coreTeam);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coreTeams;
    }

    public List<Department> getDepartmentsByVolunteerId(int volunteerId) {
        List<Department> departments = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT d.departmentid, d.departmentname FROM departments d " +
                    "JOIN volunteersdepartments vd ON d.departmentid = vd.departmentid " +
                    "WHERE vd.volunteerid = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, volunteerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int departmentId = resultSet.getInt("departmentid");
                        String departmentName = resultSet.getString("departmentname");

                        Department department = new Department(departmentId, departmentName);
                        departments.add(department);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }

    public User getUserByVolunteer(Volunteer volunteer) {
        User user = null;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM users WHERE volunteer_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, volunteer.getId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String role = resultSet.getString("role");

                        user = new User(userId, username, password, volunteer.getId(), role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
