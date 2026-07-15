package com.ngo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ngo.model.DonationRequest;
import com.ngo.util.DBUtil;

public class DonationRequestDAO {

    // Add Donation Request
    public boolean addRequest(DonationRequest request) {

        boolean status = false;
        String sql = "INSERT INTO donation_requests(ngo_id,title,description,target_amount,collected_amount,status) VALUES(?,?,?,?,?,?)";
        System.out.println("[CRUD - INSERT] Table: donation_requests | SQL: " + sql + " | Params: ngo_id=" + request.getNgoId() + ", title='" + request.getTitle() + "', target_amount=" + request.getTargetAmount());

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, request.getNgoId());
            ps.setString(2, request.getTitle());
            ps.setString(3, request.getDescription());
            ps.setDouble(4, request.getTargetAmount());
            ps.setDouble(5, request.getCollectedAmount());
            ps.setString(6, request.getStatus());

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

    // Get All Requests
    public ArrayList<DonationRequest> getAllRequests() {

        ArrayList<DonationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM donation_requests ORDER BY id DESC";
        System.out.println("[CRUD - SELECT] Table: donation_requests | SQL: " + sql);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                DonationRequest request = new DonationRequest();

                request.setId(rs.getInt("id"));
                request.setNgoId(rs.getInt("ngo_id"));
                request.setTitle(rs.getString("title"));
                request.setDescription(rs.getString("description"));
                request.setTargetAmount(rs.getDouble("target_amount"));
                request.setCollectedAmount(rs.getDouble("collected_amount"));
                request.setStatus(rs.getString("status"));

                list.add(request);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Get Request By ID
    public DonationRequest getRequestById(int id) {

        DonationRequest request = null;
        String sql = "SELECT * FROM donation_requests WHERE id=?";
        System.out.println("[CRUD - SELECT] Table: donation_requests | SQL: " + sql + " | Params: id=" + id);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                request = new DonationRequest();

                request.setId(rs.getInt("id"));
                request.setNgoId(rs.getInt("ngo_id"));
                request.setTitle(rs.getString("title"));
                request.setDescription(rs.getString("description"));
                request.setTargetAmount(rs.getDouble("target_amount"));
                request.setCollectedAmount(rs.getDouble("collected_amount"));
                request.setStatus(rs.getString("status"));

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    // Get Requests By NGO
    public ArrayList<DonationRequest> getRequestsByNgo(int ngoId) {

        ArrayList<DonationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM donation_requests WHERE ngo_id=? ORDER BY id DESC";
        System.out.println("[CRUD - SELECT] Table: donation_requests | SQL: " + sql + " | Params: ngo_id=" + ngoId);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, ngoId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                DonationRequest request = new DonationRequest();

                request.setId(rs.getInt("id"));
                request.setNgoId(rs.getInt("ngo_id"));
                request.setTitle(rs.getString("title"));
                request.setDescription(rs.getString("description"));
                request.setTargetAmount(rs.getDouble("target_amount"));
                request.setCollectedAmount(rs.getDouble("collected_amount"));
                request.setStatus(rs.getString("status"));

                list.add(request);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Update Request
    public boolean updateRequest(DonationRequest request) {

        boolean status = false;
        String sql = "UPDATE donation_requests SET title=?,description=?,target_amount=?,status=? WHERE id=?";
        System.out.println("[CRUD - UPDATE] Table: donation_requests | SQL: " + sql + " | Params: id=" + request.getId() + ", title='" + request.getTitle() + "', target_amount=" + request.getTargetAmount());

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, request.getTitle());
            ps.setString(2, request.getDescription());
            ps.setDouble(3, request.getTargetAmount());
            ps.setString(4, request.getStatus());
            ps.setInt(5, request.getId());

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

    // Update Collected Amount
    public boolean updateCollectedAmount(int requestId, double amount) {

        boolean status = false;
        String sql = "UPDATE donation_requests SET collected_amount = collected_amount + ? WHERE id=?";
        System.out.println("[CRUD - UPDATE] Table: donation_requests | SQL: " + sql + " | Params: id=" + requestId + ", added_amount=" + amount);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, amount);
            ps.setInt(2, requestId);

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

    // Delete Request
    public boolean deleteRequest(int id) {

        boolean status = false;
        String sql = "DELETE FROM donation_requests WHERE id=?";
        System.out.println("[CRUD - DELETE] Table: donation_requests | SQL: " + sql + " | Params: id=" + id);

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

    // Get Active Requests
    public ArrayList<DonationRequest> getActiveRequests() {

        ArrayList<DonationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM donation_requests WHERE status='ACTIVE'";
        System.out.println("[CRUD - SELECT] Table: donation_requests | SQL: " + sql);

        try {

            Connection con = DBUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                DonationRequest request = new DonationRequest();

                request.setId(rs.getInt("id"));
                request.setNgoId(rs.getInt("ngo_id"));
                request.setTitle(rs.getString("title"));
                request.setDescription(rs.getString("description"));
                request.setTargetAmount(rs.getDouble("target_amount"));
                request.setCollectedAmount(rs.getDouble("collected_amount"));
                request.setStatus(rs.getString("status"));

                list.add(request);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}