package com.ngo.dao;

import com.ngo.model.MaterialRequest;
import com.ngo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * MaterialRequestDAO – CRUD for material_requests table
 */
public class MaterialRequestDAO {

    public int create(MaterialRequest req) throws Exception {
        String sql = "INSERT INTO material_requests (ngo_id, beneficiary_id, item_name, description, quantity_needed, quantity_received, status, created_date) " +
                     "VALUES (?, ?, ?, ?, ?, 0, 'OPEN', CURDATE())";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (req.getNgoId() > 0) ps.setInt(1, req.getNgoId());
            else ps.setNull(1, java.sql.Types.INTEGER);
            
            if (req.getBeneficiaryId() > 0) ps.setInt(2, req.getBeneficiaryId());
            else ps.setNull(2, java.sql.Types.INTEGER);
            
            ps.setString(3, req.getItemName());
            ps.setString(4, req.getDescription());
            ps.setInt(5, req.getQuantityNeeded());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<MaterialRequest> getAll() throws Exception {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT mr.*, u.name AS ngo_name FROM material_requests mr " +
                     "JOIN users u ON mr.ngo_id = u.id ORDER BY mr.created_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<MaterialRequest> getOpen() throws Exception {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT mr.*, n.name AS ngo_name, b.name AS beneficiary_name FROM material_requests mr " +
                     "LEFT JOIN users n ON mr.ngo_id = n.id " +
                     "LEFT JOIN users b ON mr.beneficiary_id = b.id " +
                     "WHERE mr.status = 'OPEN' ORDER BY mr.created_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<MaterialRequest> getByNgo(int ngoId) throws Exception {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT mr.*, u.name AS ngo_name, NULL AS beneficiary_name FROM material_requests mr " +
                     "JOIN users u ON mr.ngo_id = u.id WHERE mr.ngo_id = ? ORDER BY mr.created_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<MaterialRequest> getByBeneficiary(int beneficiaryId) throws Exception {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT mr.*, NULL AS ngo_name, u.name AS beneficiary_name FROM material_requests mr " +
                     "JOIN users u ON mr.beneficiary_id = u.id WHERE mr.beneficiary_id = ? ORDER BY mr.created_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, beneficiaryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean fulfill(int requestId, int quantity, int donorId, String itemName) throws Exception {
        boolean success = false;
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                // Update Material Request
                String sql = "UPDATE material_requests SET quantity_received = quantity_received + ?, " +
                             "status = CASE WHEN quantity_received + ? >= quantity_needed THEN 'FULFILLED' ELSE status END " +
                             "WHERE request_id = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, quantity);
                    ps.setInt(3, requestId);
                    ps.executeUpdate();
                }

                // Log into Material Donations for Donor tracking
                String logSql = "INSERT INTO material_donations (donor_id, item_description, quantity, donation_date) VALUES (?, ?, ?, CURDATE())";
                try (PreparedStatement logPs = con.prepareStatement(logSql)) {
                    logPs.setInt(1, donorId);
                    logPs.setString(2, "Fulfillment: " + itemName); // Tagging it
                    logPs.setInt(3, quantity);
                    logPs.executeUpdate();
                }

                con.commit();
                success = true;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
        return success;
    }

    public boolean updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE material_requests SET status = ? WHERE request_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM material_requests WHERE request_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private MaterialRequest mapRow(ResultSet rs) throws SQLException {
        MaterialRequest r = new MaterialRequest();
        r.setRequestId(rs.getInt("request_id"));
        r.setNgoId(rs.getInt("ngo_id"));
        r.setBeneficiaryId(rs.getInt("beneficiary_id"));
        
        try { r.setNgoName(rs.getString("ngo_name")); } catch(SQLException e) {}
        try { r.setBeneficiaryName(rs.getString("beneficiary_name")); } catch(SQLException e) {}
        
        r.setItemName(rs.getString("item_name"));
        r.setDescription(rs.getString("description"));
        r.setQuantityNeeded(rs.getInt("quantity_needed"));
        r.setQuantityReceived(rs.getInt("quantity_received"));
        r.setStatus(rs.getString("status"));
        r.setCreatedDate(rs.getDate("created_date"));
        return r;
    }
}
