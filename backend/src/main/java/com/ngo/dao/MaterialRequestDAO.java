package com.ngo.dao;

import com.ngo.model.MaterialRequest;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * MaterialRequestDAO - All database operations for MaterialRequest
 */
public class MaterialRequestDAO {

    public boolean create(MaterialRequest mr) {
        String sql = "INSERT INTO material_requests (ngo_id,item_name,category,description,quantity_needed,quantity_received,delivery_location,deadline,status,created_date) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mr.getNgoId()); ps.setString(2, mr.getItemName());
            ps.setString(3, mr.getCategory()); ps.setString(4, mr.getDescription());
            ps.setInt(5, mr.getQuantityNeeded()); ps.setInt(6, mr.getQuantityReceived());
            ps.setString(7, mr.getDeliveryLocation()); ps.setObject(8, mr.getDeadline());
            ps.setString(9, mr.getStatus() != null ? mr.getStatus() : "OPEN");
            ps.setObject(10, mr.getCreatedDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<MaterialRequest> getAll() {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM material_requests ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<MaterialRequest> getOpen() {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM material_requests WHERE status='OPEN' ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<MaterialRequest> getByNgo(int ngoId) {
        List<MaterialRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM material_requests WHERE ngo_id=? ORDER BY request_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMR(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public MaterialRequest getById(int id) {
        String sql = "SELECT * FROM material_requests WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapMR(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateReceivedQty(int id, int qty) {
        String sql = "UPDATE material_requests SET quantity_received = quantity_received + ? WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE material_requests SET status=? WHERE request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM material_requests";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private MaterialRequest mapMR(ResultSet rs) throws SQLException {
        MaterialRequest mr = new MaterialRequest();
        mr.setRequestId(rs.getInt("request_id"));
        mr.setNgoId(rs.getInt("ngo_id"));
        mr.setItemName(rs.getString("item_name"));
        mr.setCategory(rs.getString("category"));
        mr.setDescription(rs.getString("description"));
        mr.setQuantityNeeded(rs.getInt("quantity_needed"));
        mr.setQuantityReceived(rs.getInt("quantity_received"));
        mr.setDeliveryLocation(rs.getString("delivery_location"));
        Date d = rs.getDate("deadline");
        if (d != null) mr.setDeadline(d.toLocalDate());
        mr.setStatus(rs.getString("status"));
        Date cd = rs.getDate("created_date");
        if (cd != null) mr.setCreatedDate(cd.toLocalDate());
        return mr;
    }
}
