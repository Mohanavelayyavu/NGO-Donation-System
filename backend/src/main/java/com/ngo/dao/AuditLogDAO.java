package com.ngo.dao;

import com.ngo.model.AuditLog;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * AuditLogDAO - All database operations for AuditLog
 */
public class AuditLogDAO {

    public boolean log(int adminId, String action, String details, int targetId, String targetType) {
        String sql = "INSERT INTO audit_logs (admin_id,action,details,target_id,target_type) VALUES (?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, adminId); ps.setString(2, action);
            ps.setString(3, details); ps.setInt(4, targetId);
            ps.setString(5, targetType);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<AuditLog> getAll() {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY log_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapLog(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<AuditLog> getRecent(int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY log_id DESC LIMIT ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapLog(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private AuditLog mapLog(ResultSet rs) throws SQLException {
        AuditLog a = new AuditLog();
        a.setLogId(rs.getInt("log_id"));
        a.setAdminId(rs.getInt("admin_id"));
        a.setAction(rs.getString("action"));
        a.setDetails(rs.getString("details"));
        a.setTargetId(rs.getInt("target_id"));
        a.setTargetType(rs.getString("target_type"));
        Timestamp ts = rs.getTimestamp("created_date");
        if (ts != null) a.setCreatedDate(ts.toLocalDateTime());
        return a;
    }
}
