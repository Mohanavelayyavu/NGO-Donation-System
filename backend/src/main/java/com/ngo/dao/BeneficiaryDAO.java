package com.ngo.dao;

import com.ngo.model.Beneficiary;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * BeneficiaryDAO - All database operations for Beneficiary
 */
public class BeneficiaryDAO {

    public boolean create(Beneficiary b) {
        String sql = "INSERT INTO beneficiaries (user_id,ngo_id,verification_status,contact_details,address,family_details,income_info) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getUserId()); ps.setInt(2, b.getNgoId());
            ps.setString(3, b.getVerificationStatus() != null ? b.getVerificationStatus() : "PENDING");
            ps.setString(4, b.getContactDetails()); ps.setString(5, b.getAddress());
            ps.setString(6, b.getFamilyDetails()); ps.setString(7, b.getIncomeInfo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Beneficiary> getPending() {
        List<Beneficiary> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiaries WHERE verification_status='PENDING'";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBen(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Beneficiary> getAll() {
        List<Beneficiary> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiaries ORDER BY id";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBen(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Beneficiary> getByNgo(int ngoId) {
        List<Beneficiary> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiaries WHERE ngo_id=? ORDER BY id";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBen(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean verify(int id) {
        String sql = "UPDATE beneficiaries SET verification_status='VERIFIED' WHERE id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean reject(int id) {
        String sql = "UPDATE beneficiaries SET verification_status='REJECTED' WHERE id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public Beneficiary getById(int id) {
        String sql = "SELECT * FROM beneficiaries WHERE id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapBen(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM beneficiaries";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private Beneficiary mapBen(ResultSet rs) throws SQLException {
        Beneficiary b = new Beneficiary();
        b.setId(rs.getInt("id"));
        b.setUserId(rs.getInt("user_id"));
        b.setNgoId(rs.getInt("ngo_id"));
        b.setVerificationStatus(rs.getString("verification_status"));
        b.setContactDetails(rs.getString("contact_details"));
        b.setAddress(rs.getString("address"));
        b.setFamilyDetails(rs.getString("family_details"));
        b.setIncomeInfo(rs.getString("income_info"));
        return b;
    }
}
