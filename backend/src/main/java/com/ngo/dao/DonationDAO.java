package com.ngo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ngo.model.Donation;
import com.ngo.util.DBUtil;

public class DonationDAO {

    // Add Donation
    public boolean donate(Donation donation) {

        boolean status = false;
        String sql = "INSERT INTO donations(donor_id,request_id,amount,donation_date) VALUES(?,?,?,?)";
        System.out.println("[CRUD - INSERT] Table: donations | SQL: " + sql + " | Params: donor_id=" + donation.getDonorId() + ", request_id=" + donation.getRequestId() + ", amount=" + donation.getAmount());

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, donation.getDonorId());
            ps.setInt(2, donation.getRequestId());
            ps.setDouble(3, donation.getAmount());
            if (donation.getDonationDate() == null || donation.getDonationDate().trim().isEmpty()) {
                ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            } else {
                ps.setString(4, donation.getDonationDate());
            }

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

    // Get All Donations
    public ArrayList<Donation> getAllDonations() {

        ArrayList<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations ORDER BY id DESC";
        System.out.println("[CRUD - SELECT] Table: donations | SQL: " + sql);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Donation donation = new Donation();

                donation.setId(rs.getInt("id"));
                donation.setDonorId(rs.getInt("donor_id"));
                donation.setRequestId(rs.getInt("request_id"));
                donation.setAmount(rs.getDouble("amount"));
                donation.setDonationDate(rs.getString("donation_date"));

                list.add(donation);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    // Get Donation By ID
    public Donation getDonationById(int id) {

        Donation donation = null;
        String sql = "SELECT * FROM donations WHERE id=?";
        System.out.println("[CRUD - SELECT] Table: donations | SQL: " + sql + " | Params: id=" + id);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                donation = new Donation();

                donation.setId(rs.getInt("id"));
                donation.setDonorId(rs.getInt("donor_id"));
                donation.setRequestId(rs.getInt("request_id"));
                donation.setAmount(rs.getDouble("amount"));
                donation.setDonationDate(rs.getString("donation_date"));

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return donation;

    }

    // Get Donations By Donor
    public ArrayList<Donation> getDonationsByDonor(int donorId) {

        ArrayList<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE donor_id=? ORDER BY donation_date DESC";
        System.out.println("[CRUD - SELECT] Table: donations | SQL: " + sql + " | Params: donor_id=" + donorId);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, donorId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Donation donation = new Donation();

                donation.setId(rs.getInt("id"));
                donation.setDonorId(rs.getInt("donor_id"));
                donation.setRequestId(rs.getInt("request_id"));
                donation.setAmount(rs.getDouble("amount"));
                donation.setDonationDate(rs.getString("donation_date"));

                list.add(donation);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    // Get Donations By Request
    public ArrayList<Donation> getDonationsByRequest(int requestId) {

        ArrayList<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE request_id=? ORDER BY donation_date DESC";
        System.out.println("[CRUD - SELECT] Table: donations | SQL: " + sql + " | Params: request_id=" + requestId);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, requestId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Donation donation = new Donation();

                donation.setId(rs.getInt("id"));
                donation.setDonorId(rs.getInt("donor_id"));
                donation.setRequestId(rs.getInt("request_id"));
                donation.setAmount(rs.getDouble("amount"));
                donation.setDonationDate(rs.getString("donation_date"));

                list.add(donation);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    // Get Total Donation Amount
    public double getTotalDonationAmount() {

        double total = 0;
        String sql = "SELECT SUM(amount) AS total FROM donations";
        System.out.println("[CRUD - SELECT] Table: donations | SQL: " + sql);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                total = rs.getDouble("total");

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;

    }

    // Get Total Donation For One Request
    public double getDonationAmountByRequest(int requestId) {

        double total = 0;
        String sql = "SELECT SUM(amount) AS total FROM donations WHERE request_id=?";
        System.out.println("[CRUD - SELECT] Table: donations | SQL: " + sql + " | Params: request_id=" + requestId);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, requestId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                total = rs.getDouble("total");

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;

    }

    // Delete Donation
    public boolean deleteDonation(int id) {

        boolean status = false;
        String sql = "DELETE FROM donations WHERE id=?";
        System.out.println("[CRUD - DELETE] Table: donations | SQL: " + sql + " | Params: id=" + id);

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

}