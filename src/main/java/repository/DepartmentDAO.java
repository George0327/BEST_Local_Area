package repository;

import model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();

        String query = "SELECT departmentid, departmentname FROM departments";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int departmentId = resultSet.getInt("departmentid");
                String departmentName = resultSet.getString("departmentname");

                Department department = new Department(departmentId, departmentName);
                departments.add(department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }

    public void updateDepartment(Department department) {
        String updateDepartmentQuery = "UPDATE departments SET departmentname = ? WHERE departmentid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement updateDepartmentStatement = connection.prepareStatement(updateDepartmentQuery)) {

            updateDepartmentStatement.setString(1, department.getName());
            updateDepartmentStatement.setInt(2, department.getId());

            int affectedRows = updateDepartmentStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows updated. Check if the department ID exists.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDepartment(String departmentName) {
        String addDepartmentQuery = "INSERT INTO departments (departmentname) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement addDepartmentStatement = connection.prepareStatement(addDepartmentQuery, Statement.RETURN_GENERATED_KEYS)) {

            addDepartmentStatement.setString(1, departmentName);

            addDepartmentStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDepartment(int departmentId) {
        String deleteDepartmentSql = "DELETE FROM departments WHERE departmentid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement deleteDepartmentStatement = connection.prepareStatement(deleteDepartmentSql)) {

            deleteDepartmentStatement.setInt(1, departmentId);
            deleteDepartmentStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
