package com.ngo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.ngo.dao.UserDAO;
import com.ngo.model.User;

/*
 * User Service
 */

@Service
public class UserService {

    private UserDAO dao = new UserDAO();

    // Register User
    public boolean registerUser(User user) {

        return dao.registerUser(user);

    }

    // Login
    public User loginUser(User user) {

        return dao.loginUser(
                user.getEmail(),
                user.getPassword(),
                user.getRole());

    }

    // Get All Users
    public ArrayList<User> getAllUsers() {

        return dao.getAllUsers();

    }

    // Get User By Id
    public User getUserById(int id) {

        return dao.getUserById(id);

    }

    // Update User
    public boolean updateUser(User user) {

        return dao.updateUser(user);

    }

    // Delete User
    public boolean deleteUser(int id) {

        return dao.deleteUser(id);

    }

}