package com.subitshar.journeytime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDbAccess {

    public void addAccount(Account account) throws Exception {
    	  final String INSERT_ACCOUNT_QUERY = "INSERT INTO Account (userName, password, accountType) VALUES (?, ?, ?)";
        try (Connection con = Db_connection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ACCOUNT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getUserName());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getAccountType());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int lastInsertedAccountNo = generatedKeys.getInt(1);
                account.setUserId(lastInsertedAccountNo);
                System.out.println(lastInsertedAccountNo);
            }
        } catch (SQLException e) {
            throw new Exception("Error adding account", e);
        }
    }

    public void displayAccountDetails(Account account) throws SQLException {
        // Define the query to fetch account details
        final String DISPLAY_ACCOUNT_DETAILS_QUERY = 
            "SELECT user_id, userName, accountType FROM Account WHERE user_id = ?";

        // Null check for account object
        if (account == null || account.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid account details provided.");
        }

        // Use try-with-resources to handle JDBC objects
        try (Connection con = Db_connection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(DISPLAY_ACCOUNT_DETAILS_QUERY)) {

            // Set the user ID in the query
            preparedStatement.setInt(1, account.getUserId());

            // Execute the query
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Display the account details
                    System.out.println("----------------Account Details!---------------");
                    System.out.println("Hi! " + rs.getString("userName"));
                    System.out.println("Your User ID is: " + rs.getInt("user_id"));
                    System.out.println("Account Type: " + rs.getString("accountType"));
                } else {
                    System.out.println("No account found with User ID: " + account.getUserId());
                }
            }
        } catch (SQLException e) {
            // Throw the exception with additional context
            throw new SQLException("Error displaying account details", e);
        }
    }

    public boolean accountValidation(int userId, String password) throws Exception {
    	  final String VALIDATE_ACCOUNT_QUERY = "SELECT password FROM Account WHERE user_id = ?";
        try (Connection con = Db_connection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(VALIDATE_ACCOUNT_QUERY)) {

            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next() && rs.getString("password").equals(password);
        } catch (SQLException e) {
            throw new Exception("Error validating account", e);
        }
    }

    public String getAccountType(int userId) throws Exception {
    	 final String GET_ACCOUNT_TYPE_QUERY = "SELECT accountType FROM Account WHERE user_id = ?";
        try (Connection con = Db_connection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(GET_ACCOUNT_TYPE_QUERY)) {

            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next() ? rs.getString("accountType") : "";
        } catch (SQLException e) {
            throw new Exception("Error getting account type", e);
        }
    }

    public void closeAccount(int userId) throws Exception {
        try (Connection con = Db_connection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM Account WHERE user_id = ?")) {
        	
            BookingDbAccess bookingDbAccess = new BookingDbAccess();
            bookingDbAccess.cancelBookingByUserId(userId);
            
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error closing account", e);
        }
    }

    public String getUserName(int userId) throws Exception {
        try (Connection con = Db_connection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT userName FROM Account WHERE user_id = ?")) {

            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next() ? rs.getString("userName") : "";
        } catch (SQLException e) {
            throw new Exception("Error getting user name", e);
        }
    }
}
