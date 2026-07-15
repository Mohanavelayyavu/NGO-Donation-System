package com.ngo.dao;

import com.ngo.model.Donation;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DonationDAO - All database operations for Donation
 */
public class DonationDAO {

    public boolean create(Donation d) {
        String sql = "INSERT INTO donations (donor_id,request_id,amount,payment_status,donation_status,donation_date) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getDonorId()); ps.setInt(2, d.getRequestId());
            ps.setDouble(3, d.getAmount());
            ps.setString(4, d.getPaymentStatus() != null ? d.getPaymentStatus() : "SUCCESS");
            ps.setString(5, d.getDonationStatus() != null ? d.getDonationStatus() : "COMPLETED");
            ps.setObject(6, d.getDonationDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Donation> getAll() {
        List<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations ORDER BY donation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapDonation(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Donation> getByDonor(int donorId) {
        List<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE donor_id=? ORDER BY donation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapDonation(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Donation> getByRequest(int requestId) {
        List<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE request_id=? ORDER BY donation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapDonation(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Donation getById(int id) {
        String sql = "SELECT * FROM donations WHERE donation_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapDonation(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public double getTotalAmount() {
        String sql = "SELECT COALESCE(SUM(amount),0) FROM donations";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0.0;
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM donations";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getTotalAmountByDonor(int donorId) {
        String sql = "SELECT COALESCE(SUM(amount),0) FROM donations WHERE donor_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0.0;
    }

    private Donation mapDonation(ResultSet rs) throws SQLException {
        Donation d = new Donation();
        d.setDonationId(rs.getInt("donation_id"));
        d.setDonorId(rs.getInt("donor_id"));
        d.setRequestId(rs.getInt("request_id"));
        d.setAmount(rs.getDouble("amount"));
        d.setPaymentStatus(rs.getString("payment_status"));
        d.setDonationStatus(rs.getString("donation_status"));
        Date dt = rs.getDate("donation_date");
        if (dt != null) d.setDonationDate(dt.toLocalDate());
        return d;
    }
}
