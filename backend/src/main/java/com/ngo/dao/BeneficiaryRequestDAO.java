package com.ngo.dao;

import com.ngo.model.BeneficiaryRequest;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * BeneficiaryRequestDAO - All database operations for BeneficiaryRequest
 */
public class BeneficiaryRequestDAO {

    public boolean create(BeneficiaryRequest br) {
        String sql = "INSERT INTO beneficiary_requests (beneficiary_id,assistance_category,item_name,requested_amount,requested_quantity,reason,urgency,status,created_date) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, br.getBeneficiaryId()); ps.setString(2, br.getAssistanceCategory());
            ps.setString(3, br.getItemName()); ps.setDouble(4, br.getRequestedAmount());
            ps.setInt(5, br.getRequestedQuantity()); ps.setString(6, br.getReason());
            ps.setString(7, br.getUrgency() != null ? br.getUrgency() : "NORMAL");
            ps.setString(8, br.getStatus() != null ? br.getStatus() : "PENDING");
            ps.setObject(9, br.getCreatedDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<BeneficiaryRequest> getAll() {
        List<BeneficiaryRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiary_requests ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<BeneficiaryRequest> getByBeneficiary(int beneficiaryId) {
        List<BeneficiaryRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiary_requests WHERE beneficiary_id=? ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, beneficiaryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<BeneficiaryRequest> getPending() {
        List<BeneficiaryRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiary_requests WHERE status='PENDING' ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean approve(int id) {
        String sql = "UPDATE beneficiary_requests SET status='APPROVED' WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM beneficiary_requests";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private BeneficiaryRequest mapBR(ResultSet rs) throws SQLException {
        BeneficiaryRequest br = new BeneficiaryRequest();
        br.setRequestId(rs.getInt("request_id"));
        br.setBeneficiaryId(rs.getInt("beneficiary_id"));
        br.setAssistanceCategory(rs.getString("assistance_category"));
        br.setItemName(rs.getString("item_name"));
        br.setRequestedAmount(rs.getDouble("requested_amount"));
        br.setRequestedQuantity(rs.getInt("requested_quantity"));
        br.setReason(rs.getString("reason"));
        br.setUrgency(rs.getString("urgency"));
        br.setStatus(rs.getString("status"));
        Date d = rs.getDate("created_date");
        if (d != null) br.setCreatedDate(d.toLocalDate());
        return br;
    }
}
