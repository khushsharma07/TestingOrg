package com.service;

import com.model.User;
import com.repository.UserDAO;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() throws SQLException {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String employeeId, String password) {
        return userDAO.getUserByEmployeeIdAndPassword(employeeId, password);
    }
}