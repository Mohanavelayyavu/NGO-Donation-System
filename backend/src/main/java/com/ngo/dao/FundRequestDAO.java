package com.ngo.dao;

import com.ngo.model.FundRequest;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * FundRequestDAO - All database operations for FundRequest
 */
public class FundRequestDAO {

    public boolean create(FundRequest fr) {
        String sql = "INSERT INTO fund_requests (ngo_id,title,description,category,target_amount,collected_amount,status,created_date) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, fr.getNgoId()); ps.setString(2, fr.getTitle());
            ps.setString(3, fr.getDescription()); ps.setString(4, fr.getCategory());
            ps.setDouble(5, fr.getTargetAmount()); ps.setDouble(6, fr.getCollectedAmount());
            ps.setString(7, fr.getStatus() != null ? fr.getStatus() : "OPEN");
            ps.setObject(8, fr.getCreatedDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<FundRequest> getAll() {
        List<FundRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM fund_requests ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<FundRequest> getOpen() {
        List<FundRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM fund_requests WHERE status='OPEN' ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<FundRequest> getByNgo(int ngoId) {
        List<FundRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM fund_requests WHERE ngo_id=? ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public FundRequest getById(int id) {
        String sql = "SELECT * FROM fund_requests WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapFR(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateCollectedAmount(int requestId, double amount) {
        String sql = "UPDATE fund_requests SET collected_amount = collected_amount + ? WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount); ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE fund_requests SET status=? WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM fund_requests";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int getCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM fund_requests WHERE status=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private FundRequest mapFR(ResultSet rs) throws SQLException {
        FundRequest fr = new FundRequest();
        fr.setRequestId(rs.getInt("request_id"));
        fr.setNgoId(rs.getInt("ngo_id"));
        fr.setTitle(rs.getString("title"));
        fr.setDescription(rs.getString("description"));
        fr.setCategory(rs.getString("category"));
        fr.setTargetAmount(rs.getDouble("target_amount"));
        fr.setCollectedAmount(rs.getDouble("collected_amount"));
        fr.setStatus(rs.getString("status"));
        Date d = rs.getDate("created_date");
        if (d != null) fr.setCreatedDate(d.toLocalDate());
        return fr;
    }
}
