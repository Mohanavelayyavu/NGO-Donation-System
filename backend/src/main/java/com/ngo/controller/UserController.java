package com.ngo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.model.User;
import com.ngo.service.UserService;

/*
 * User Controller
 */

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService service;

    // Register

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        boolean status = service.registerUser(user);

        if(status) {
            return "Registration Successful";
        }

        return "Registration Failed";
    }

    // Login

    @PostMapping("/login")
    public User login(@RequestBody User user) {

        return service.loginUser(user);

    }

    // View Users

    @GetMapping("/users")
    public ArrayList<User> getUsers() {

        return service.getAllUsers();

    }

    // User By Id

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {

        return service.getUserById(id);

    }

    // Update User

    @PutMapping("/users")
    public String updateUser(@RequestBody User user) {

        boolean status = service.updateUser(user);

        if(status) {
            return "User Updated";
        }

        return "Update Failed";

    }

    // Delete User

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {

        boolean status = service.deleteUser(id);

        if(status) {
            return "User Deleted";
        }

        return "Delete Failed";

    }

}