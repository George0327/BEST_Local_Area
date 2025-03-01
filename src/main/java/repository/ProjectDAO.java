package repository;

import model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;
    public List<Project> getAllProjectsWithCoreTeamNames() {
        List<Project> projects = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT p.projectid, p.projectname, p.coreteamid, c.teamname " +
                     "FROM projects p " +
                     "LEFT JOIN coreteams c ON p.coreteamid = c.coreteamid")) {

            while (resultSet.next()) {
                int projectId = resultSet.getInt("projectid");
                String projectName = resultSet.getString("projectname");
                int coreTeamId = resultSet.getInt("coreteamid");
                String coreTeamName = resultSet.getString("teamname");

                Project project = new Project(projectId, projectName, coreTeamId, coreTeamName);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    public void updateProject(Project project) {
        String updateProjectQuery = "UPDATE projects SET projectname = ?, coreteamid = ? WHERE projectid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement updateProjectStatement = connection.prepareStatement(updateProjectQuery)) {

            updateProjectStatement.setString(1, project.getProjectName());
            updateProjectStatement.setInt(2, project.getCtId());
            updateProjectStatement.setInt(3, project.getId());

            int affectedRows = updateProjectStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows updated. Check if the project ID exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addProject(String projectName) {
        String addProjectQuery = "INSERT INTO projects (projectname) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement addProjectStatement = connection.prepareStatement(addProjectQuery, Statement.RETURN_GENERATED_KEYS)) {

            addProjectStatement.setString(1, projectName);

            addProjectStatement.executeUpdate();

            // If needed, retrieve the auto-generated project ID
            try (ResultSet generatedKeys = addProjectStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int projectId = generatedKeys.getInt(1);
                    // You can handle the generated project ID as needed
                    System.out.println("Project added with ID: " + projectId);
                } else {
                    System.out.println("Failed to retrieve the generated project ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(int projectId) {
        String deleteProjectSql = "DELETE FROM projects WHERE projectid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement deleteProjectStatement = connection.prepareStatement(deleteProjectSql)) {

            deleteProjectStatement.setInt(1, projectId);
            deleteProjectStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
