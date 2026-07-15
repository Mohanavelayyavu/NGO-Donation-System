package com.ngo.model;

import java.time.LocalDate;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private LocalDate createdDate;

    public User() {}

    public User(int id, String name, String email, String password, String role, LocalDate createdDate) {
        this.id = id; this.name = name; this.email = email;
        this.password = password; this.role = role; this.createdDate = createdDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
}
