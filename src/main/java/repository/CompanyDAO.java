package repository;

import model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    private static final String JDBC_URL = DataBaseConfig.JDBC_URL;
    private static final String JDBC_USER = DataBaseConfig.JDBC_USER;
    private static final String JDBC_PASSWORD = DataBaseConfig.JDBC_PASSWORD;

    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM companies")) {

            while (resultSet.next()) {
                int companyId = resultSet.getInt("companyid");
                String companyName = resultSet.getString("companyname");

                Company company = new Company(companyId, companyName);
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    public void addCompany(String companyName) {
        String addCompanyQuery = "INSERT INTO companies (companyname) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement addCompanyStatement = connection.prepareStatement(addCompanyQuery, Statement.RETURN_GENERATED_KEYS)) {

            addCompanyStatement.setString(1, companyName);

            addCompanyStatement.executeUpdate();

            // If needed, retrieve the auto-generated company ID
            try (ResultSet generatedKeys = addCompanyStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int companyId = generatedKeys.getInt(1);
                    // You can handle the generated company ID as needed
                    System.out.println("Company added with ID: " + companyId);
                } else {
                    System.out.println("Failed to retrieve the generated company ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCompany(Company company) {
        String updateCompanyQuery = "UPDATE companies SET companyname = ? WHERE companyid = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement updateCompanyStatement = connection.prepareStatement(updateCompanyQuery)) {

            updateCompanyStatement.setString(1, company.getCompanyName());
            updateCompanyStatement.setInt(2, company.getId());

            int affectedRows = updateCompanyStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows updated. Check if the ID exists.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCompany(int companyId) {
        String deleteCompanySql = "DELETE FROM companies WHERE companyid = ?;";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement deleteCompanyStatement = connection.prepareStatement(deleteCompanySql)) {

            deleteCompanyStatement.setInt(1, companyId);
            deleteCompanyStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

