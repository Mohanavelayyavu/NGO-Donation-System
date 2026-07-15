package com.ngo.dao;

import com.ngo.model.MaterialDonation;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * MaterialDonationDAO - All database operations for MaterialDonation
 */
public class MaterialDonationDAO {

    public boolean pledge(MaterialDonation md) {
        String sql = "INSERT INTO material_donations (donor_id,request_id,item_name,quantity,damaged_quantity,courier_name,tracking_number,expected_delivery_date,delivery_status,receipt_status,donation_date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, md.getDonorId()); ps.setInt(2, md.getRequestId());
            ps.setString(3, md.getItemName()); ps.setInt(4, md.getQuantity());
            ps.setInt(5, md.getDamagedQuantity());
            ps.setString(6, md.getCourierName()); ps.setString(7, md.getTrackingNumber());
            ps.setObject(8, md.getExpectedDeliveryDate());
            ps.setString(9, md.getDeliveryStatus() != null ? md.getDeliveryStatus() : "NOT_STARTED");
            ps.setString(10, md.getReceiptStatus() != null ? md.getReceiptStatus() : "PENDING");
            ps.setObject(11, md.getDonationDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<MaterialDonation> getAll() {
        List<MaterialDonation> list = new ArrayList<>();
        String sql = "SELECT * FROM material_donations ORDER BY donation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMD(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<MaterialDonation> getByDonor(int donorId) {
        List<MaterialDonation> list = new ArrayList<>();
        String sql = "SELECT * FROM material_donations WHERE donor_id=? ORDER BY donation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMD(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<MaterialDonation> getByRequest(int requestId) {
        List<MaterialDonation> list = new ArrayList<>();
        String sql = "SELECT * FROM material_donations WHERE request_id=? ORDER BY donation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMD(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public MaterialDonation getById(int id) {
        String sql = "SELECT * FROM material_donations WHERE donation_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapMD(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateLogistics(int id, String courier, String tracking, String expectedDate) {
        String sql = "UPDATE material_donations SET courier_name=?, tracking_number=?, expected_delivery_date=?, delivery_status='IN_TRANSIT' WHERE donation_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courier); ps.setString(2, tracking);
            ps.setString(3, expectedDate); ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean markDelivered(int id, int damagedQty) {
        String sql = "UPDATE material_donations SET delivery_status='DELIVERED', receipt_status='RECEIVED', damaged_quantity=? WHERE donation_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, damagedQty); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM material_donations";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private MaterialDonation mapMD(ResultSet rs) throws SQLException {
        MaterialDonation md = new MaterialDonation();
        md.setDonationId(rs.getInt("donation_id"));
        md.setDonorId(rs.getInt("donor_id"));
        md.setRequestId(rs.getInt("request_id"));
        md.setItemName(rs.getString("item_name"));
        md.setQuantity(rs.getInt("quantity"));
        md.setDamagedQuantity(rs.getInt("damaged_quantity"));
        md.setCourierName(rs.getString("courier_name"));
        md.setTrackingNumber(rs.getString("tracking_number"));
        Date ed = rs.getDate("expected_delivery_date");
        if (ed != null) md.setExpectedDeliveryDate(ed.toLocalDate());
        md.setDeliveryStatus(rs.getString("delivery_status"));
        md.setReceiptStatus(rs.getString("receipt_status"));
        Date dd = rs.getDate("donation_date");
        if (dd != null) md.setDonationDate(dd.toLocalDate());
        return md;
    }
}
