package com.ngo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ngo.model.Beneficiary;
import com.ngo.util.DBUtil;

/*
 * Beneficiary DAO Class
 * Handles all database operations
 */

public class BeneficiaryDAO {

    // Add Beneficiary Application
    public boolean addApplication(Beneficiary beneficiary) {

        boolean status = false;
        String sql = "INSERT INTO beneficiaries(user_id,purpose,description,required_amount,status) VALUES(?,?,?,?,?)";
        System.out.println("[CRUD - INSERT] Table: beneficiaries | SQL: " + sql + " | Params: user_id=" + beneficiary.getUserId() + ", purpose='" + beneficiary.getPurpose() + "', required_amount=" + beneficiary.getRequiredAmount());

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, beneficiary.getUserId());
            ps.setString(2, beneficiary.getPurpose());
            ps.setString(3, beneficiary.getDescription());
            ps.setDouble(4, beneficiary.getRequiredAmount());
            ps.setString(5, beneficiary.getStatus());

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

    // Get All Applications
    public List<Beneficiary> getAllApplications() {

        List<Beneficiary> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiaries";
        System.out.println("[CRUD - SELECT] Table: beneficiaries | SQL: " + sql);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Beneficiary beneficiary = new Beneficiary();

                beneficiary.setId(rs.getInt("id"));
                beneficiary.setUserId(rs.getInt("user_id"));
                beneficiary.setPurpose(rs.getString("purpose"));
                beneficiary.setDescription(rs.getString("description"));
                beneficiary.setRequiredAmount(rs.getDouble("required_amount"));
                beneficiary.setCollectedAmount(rs.getDouble("collected_amount"));
                beneficiary.setStatus(rs.getString("status"));

                list.add(beneficiary);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Get Application By ID
    public Beneficiary getApplicationById(int id) {

        Beneficiary beneficiary = null;
        String sql = "SELECT * FROM beneficiaries WHERE id=?";
        System.out.println("[CRUD - SELECT] Table: beneficiaries | SQL: " + sql + " | Params: id=" + id);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                beneficiary = new Beneficiary();

                beneficiary.setId(rs.getInt("id"));
                beneficiary.setUserId(rs.getInt("user_id"));
                beneficiary.setPurpose(rs.getString("purpose"));
                beneficiary.setDescription(rs.getString("description"));
                beneficiary.setRequiredAmount(rs.getDouble("required_amount"));
                beneficiary.setCollectedAmount(rs.getDouble("collected_amount"));
                beneficiary.setStatus(rs.getString("status"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return beneficiary;
    }

    // Update Status
    public boolean updateStatus(int id, String status) {

        boolean updated = false;
        String sql = "UPDATE beneficiaries SET status=? WHERE id=?";
        System.out.println("[CRUD - UPDATE] Table: beneficiaries | SQL: " + sql + " | Params: id=" + id + ", status='" + status + "'");

        try {

            Connection con = DBUtil.getConnection();

            String sqlUpdate = "UPDATE beneficiaries SET status=? WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sqlUpdate);

            ps.setString(1, status);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                updated = true;
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return updated;
    }

    // Donate to Beneficiary
    public boolean donateToBeneficiary(int beneficiaryId, int donorId, double amount) {
        boolean success = false;
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1. Insert into beneficiary_donations
                String insertDonation = "INSERT INTO beneficiary_donations(donor_id, beneficiary_id, amount, donation_date) VALUES(?, ?, ?, CURDATE())";
                try (PreparedStatement ps1 = con.prepareStatement(insertDonation)) {
                    ps1.setInt(1, donorId);
                    ps1.setInt(2, beneficiaryId);
                    ps1.setDouble(3, amount);
                    ps1.executeUpdate();
                }
                
                // 2. Update collected_amount in beneficiaries
                String updateBeneficiary = "UPDATE beneficiaries SET collected_amount = collected_amount + ? WHERE id = ?";
                try (PreparedStatement ps2 = con.prepareStatement(updateBeneficiary)) {
                    ps2.setDouble(1, amount);
                    ps2.setInt(2, beneficiaryId);
                    ps2.executeUpdate();
                }
                
                con.commit();
                success = true;
            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // Get donations by donor
    public List<com.ngo.model.BeneficiaryDonation> getDonationsByDonor(int donorId) {
        List<com.ngo.model.BeneficiaryDonation> list = new ArrayList<>();
        String sql = "SELECT bd.*, b.purpose FROM beneficiary_donations bd " +
                     "JOIN beneficiaries b ON bd.beneficiary_id = b.id WHERE bd.donor_id = ? ORDER BY bd.donation_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.ngo.model.BeneficiaryDonation bd = new com.ngo.model.BeneficiaryDonation();
                    bd.setId(rs.getInt("id"));
                    bd.setDonorId(rs.getInt("donor_id"));
                    bd.setBeneficiaryId(rs.getInt("beneficiary_id"));
                    bd.setPurpose(rs.getString("purpose"));
                    bd.setAmount(rs.getDouble("amount"));
                    bd.setDonationDate(rs.getDate("donation_date"));
                    list.add(bd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}