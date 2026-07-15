package com.ngo.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize() {
        System.out.println("Initializing Database schema check...");
        try (Connection con = DBUtil.getConnection()) {
            if (con == null) {
                System.err.println("Could not establish connection to the database. Schema initialization skipped.");
                return;
            }

            try (Statement stmt = con.createStatement()) {
                DatabaseMetaData dbm = con.getMetaData();
                
                // 1. Create users table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "users", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'users' table...");
                        String sql = "CREATE TABLE users(" +
                                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "name VARCHAR(100) NOT NULL, " +
                                     "email VARCHAR(100) UNIQUE NOT NULL, " +
                                     "password VARCHAR(100) NOT NULL, " +
                                     "role VARCHAR(20) NOT NULL" +
                                     ")";
                        stmt.executeUpdate(sql);
                    }
                }

                // Seed users table if empty
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("Seeding 'users' table...");
                        stmt.executeUpdate("INSERT INTO users(name,email,password,role) VALUES" +
                                "('Admin','admin@donation.org','admin123','ADMIN')," +
                                "('Rahul Kumar','rahul@gmail.com','123','DONOR')," +
                                "('Priya Sharma','priya@gmail.com','123','DONOR')," +
                                "('Helping Hands NGO','ngo@gmail.com','123','NGO')");
                    }
                }

                // 2. Create donation_requests table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "donation_requests", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'donation_requests' table...");
                        String sql = "CREATE TABLE donation_requests(" +
                                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "ngo_id INT NOT NULL, " +
                                     "title VARCHAR(150) NOT NULL, " +
                                     "description TEXT, " +
                                     "target_amount DOUBLE NOT NULL, " +
                                     "collected_amount DOUBLE DEFAULT 0, " +
                                     "status VARCHAR(30) DEFAULT 'ACTIVE', " +
                                     "FOREIGN KEY(ngo_id) REFERENCES users(id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    }
                }

                // 3. Create donations table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "donations", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'donations' table...");
                        String sql = "CREATE TABLE donations(" +
                                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "donor_id INT NOT NULL, " +
                                     "request_id INT NOT NULL, " +
                                     "amount DOUBLE NOT NULL, " +
                                     "donation_date DATE, " +
                                     "FOREIGN KEY(donor_id) REFERENCES users(id), " +
                                     "FOREIGN KEY(request_id) REFERENCES donation_requests(id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    }
                }

                // 4. Create beneficiaries table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "beneficiaries", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'beneficiaries' table...");
                        String sql = "CREATE TABLE beneficiaries(" +
                                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "user_id INT NOT NULL, " +
                                     "purpose VARCHAR(255) NOT NULL, " +
                                     "description TEXT, " +
                                     "required_amount DOUBLE NOT NULL, " +
                                     "collected_amount DOUBLE DEFAULT 0.0, " +
                                     "status VARCHAR(50) DEFAULT 'PENDING', " +
                                     "FOREIGN KEY(user_id) REFERENCES users(id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    } else {
                        // Check if collected_amount column exists, if not add it
                        try {
                            stmt.executeUpdate("ALTER TABLE beneficiaries ADD COLUMN collected_amount DOUBLE DEFAULT 0.0");
                            System.out.println("Added 'collected_amount' to 'beneficiaries'");
                        } catch (Exception e) { /* Column already exists or error */ }
                    }
                }

                // Create beneficiary_donations table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "beneficiary_donations", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'beneficiary_donations' table...");
                        String sql = "CREATE TABLE beneficiary_donations(" +
                                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "donor_id INT NOT NULL, " +
                                     "beneficiary_id INT NOT NULL, " +
                                     "amount DOUBLE NOT NULL, " +
                                     "donation_date DATE, " +
                                     "FOREIGN KEY(donor_id) REFERENCES users(id), " +
                                     "FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    }
                }
                
                // 5. Create material_donations table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "material_donations", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'material_donations' table...");
                        String sql = "CREATE TABLE material_donations(" +
                                     "material_donation_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "donor_id INT NOT NULL, " +
                                     "item_description VARCHAR(255) NOT NULL, " +
                                     "quantity INT NOT NULL DEFAULT 1, " +
                                     "donation_date DATE, " +
                                     "FOREIGN KEY(donor_id) REFERENCES users(id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    }
                }

                // 6. Create fund_expenditures table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "fund_expenditures", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'fund_expenditures' table...");
                        String sql = "CREATE TABLE fund_expenditures(" +
                                     "expenditure_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "ngo_id INT NOT NULL, " +
                                     "purpose VARCHAR(255) NOT NULL, " +
                                     "amount DOUBLE NOT NULL, " +
                                     "spent_date DATE, " +
                                     "material_donation_id INT DEFAULT NULL, " +
                                     "FOREIGN KEY(ngo_id) REFERENCES users(id), " +
                                     "FOREIGN KEY(material_donation_id) REFERENCES material_donations(material_donation_id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    }
                }

                // 7. Create material_requests table if not exists
                try (ResultSet rs = dbm.getTables(null, null, "material_requests", null)) {
                    if (!rs.next()) {
                        System.out.println("Creating 'material_requests' table...");
                        String sql = "CREATE TABLE material_requests(" +
                                     "request_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                     "ngo_id INT, " +
                                     "beneficiary_id INT, " +
                                     "item_name VARCHAR(255) NOT NULL, " +
                                     "description TEXT, " +
                                     "quantity_needed INT NOT NULL DEFAULT 1, " +
                                     "quantity_received INT NOT NULL DEFAULT 0, " +
                                     "status VARCHAR(20) DEFAULT 'OPEN', " +
                                     "created_date DATE, " +
                                     "FOREIGN KEY(ngo_id) REFERENCES users(id), " +
                                     "FOREIGN KEY(beneficiary_id) REFERENCES users(id)" +
                                     ")";
                        stmt.executeUpdate(sql);
                    } else {
                        // Alter existing table
                        try {
                            stmt.executeUpdate("ALTER TABLE material_requests MODIFY ngo_id INT NULL");
                            stmt.executeUpdate("ALTER TABLE material_requests ADD COLUMN beneficiary_id INT NULL");
                            stmt.executeUpdate("ALTER TABLE material_requests ADD CONSTRAINT fk_mr_ben FOREIGN KEY (beneficiary_id) REFERENCES users(id)");
                            System.out.println("Altered 'material_requests' to support beneficiaries");
                        } catch(Exception e) { /* already altered */ }
                    }
                }

                System.out.println("Database schema check completed successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error while initializing database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
