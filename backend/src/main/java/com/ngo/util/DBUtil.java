package com.ngo.util;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * Database Utility Class
 * Creates MySQL Connection
 */

public class DBUtil {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ngo_donation_db";

    private static final String USER = "root";

    private static final String PASSWORD = "root";

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return con;

    }

}