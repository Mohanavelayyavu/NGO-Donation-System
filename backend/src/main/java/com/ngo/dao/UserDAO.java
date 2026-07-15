package com.ngo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ngo.model.User;
import com.ngo.util.DBUtil;

public class UserDAO {

    // Register User
    public boolean registerUser(User user) {

        boolean status = false;
        String sql = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";
        System.out.println("[CRUD - INSERT] Table: users | SQL: " + sql + " | Params: name='" + user.getName() + "', email='" + user.getEmail() + "', role='" + user.getRole() + "'");

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // Login
    public User loginUser(String email, String password, String role) {

        User user = null;
        String sql = "SELECT * FROM users WHERE email=? AND password=? AND role=?";
        System.out.println("[CRUD - SELECT] Table: users | SQL: " + sql + " | Params: email='" + email + "', role='" + role + "'");

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

    }

    // Get All Users
    public ArrayList<User> getAllUsers() {

        ArrayList<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        System.out.println("[CRUD - SELECT] Table: users | SQL: " + sql);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                User user = new User();

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                list.add(user);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    // Get User By Id
    public User getUserById(int id) {

        User user = null;
        String sql = "SELECT * FROM users WHERE id=?";
        System.out.println("[CRUD - SELECT] Table: users | SQL: " + sql + " | Params: id=" + id);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

    }

    // Update User
    public boolean updateUser(User user) {

        boolean status = false;
        String sql = "UPDATE users SET name=?,email=?,password=?,role=? WHERE id=?";
        System.out.println("[CRUD - UPDATE] Table: users | SQL: " + sql + " | Params: id=" + user.getId() + ", name='" + user.getName() + "', email='" + user.getEmail() + "', role='" + user.getRole() + "'");

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;

    }

    // Delete User
    public boolean deleteUser(int id) {

        boolean status = false;
        String sql = "DELETE FROM users WHERE id=?";
        System.out.println("[CRUD - DELETE] Table: users | SQL: " + sql + " | Params: id=" + id);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;

    }

    // Check Email Exists
    public boolean emailExists(String email) {

        boolean status = false;
        String sql = "SELECT * FROM users WHERE email=?";
        System.out.println("[CRUD - SELECT] Table: users | SQL: " + sql + " | Params: email='" + email + "'");

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                status = true;
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;

    }

}