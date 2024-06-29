package com.repository;

import com.database.SQLDatabaseHelper;
import com.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    final private Connection connection;
    public UserDAO() throws SQLException {
        this.connection = SQLDatabaseHelper.getConnection();
    }

    public User getUserByEmployeeIdAndPassword(String employeeId, String password) {
        String query = "SELECT user.*, Role.RoleName FROM user " +
                "JOIN Role ON user.RoleId = role.RoleId " +
                "WHERE user.EmplyeeId = ? AND user.Password = ?";
        try (//Connection connection = SQLDatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employeeId);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapToUser(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null;
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("UserId"));
        user.setEmployeeId(resultSet.getString("EmplyeeId"));
        user.setName(resultSet.getString("Name"));
        user.setPassword(resultSet.getString("Password"));
        user.setRoleId(resultSet.getInt("RoleID"));
        user.setRoleName(resultSet.getString("RoleName"));
        return user;
    }
}