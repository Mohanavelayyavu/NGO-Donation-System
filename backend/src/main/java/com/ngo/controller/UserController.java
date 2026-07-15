package com.ngo.controller;

import com.ngo.model.User;
import com.ngo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role");
        User user = userService.loginUser(email, password, role);
        if (user != null) return ResponseEntity.ok(user);
        return ResponseEntity.status(401).body(Map.of("message","Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        boolean ok = userService.registerUser(user);
        if (ok) return ResponseEntity.ok(Map.of("message","User registered successfully"));
        return ResponseEntity.status(500).body(Map.of("message","Registration failed"));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User u = userService.getUserById(id);
        if (u != null) return ResponseEntity.ok(u);
        return ResponseEntity.status(404).body(Map.of("message","User not found"));
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/users/count")
    public ResponseEntity<?> getUserCount() {
        return ResponseEntity.ok(Map.of("total", userService.getTotalCount()));
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        boolean ok = userService.updateUser(user);
        if (ok) return ResponseEntity.ok(Map.of("message","User updated"));
        return ResponseEntity.status(500).body(Map.of("message","Update failed"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        boolean ok = userService.deleteUser(id);
        if (ok) return ResponseEntity.ok(Map.of("message","User deleted"));
        return ResponseEntity.status(500).body(Map.of("message","Delete failed"));
    }
}