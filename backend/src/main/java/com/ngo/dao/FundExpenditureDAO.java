package com.ngo.dao;

import com.ngo.model.FundExpenditure;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * FundExpenditureDAO - All database operations for FundExpenditure
 */
public class FundExpenditureDAO {

    public boolean create(FundExpenditure fe) {
        String sql = "INSERT INTO fund_expenditures (ngo_id,request_id,amount,description,proof_document,spent_date,status) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, fe.getNgoId()); ps.setInt(2, fe.getRequestId());
            ps.setDouble(3, fe.getAmount()); ps.setString(4, fe.getDescription());
            ps.setString(5, fe.getProofDocument()); ps.setObject(6, fe.getSpentDate());
            ps.setString(7, fe.getStatus() != null ? fe.getStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<FundExpenditure> getAll() {
        List<FundExpenditure> list = new ArrayList<>();
        String sql = "SELECT * FROM fund_expenditures ORDER BY expenditure_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFE(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<FundExpenditure> getByNgo(int ngoId) {
        List<FundExpenditure> list = new ArrayList<>();
        String sql = "SELECT * FROM fund_expenditures WHERE ngo_id=? ORDER BY expenditure_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFE(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<FundExpenditure> getPending() {
        List<FundExpenditure> list = new ArrayList<>();
        String sql = "SELECT * FROM fund_expenditures WHERE status='PENDING' ORDER BY expenditure_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFE(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean approve(int id) {
        String sql = "UPDATE fund_expenditures SET status='APPROVED' WHERE expenditure_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean reject(int id) {
        String sql = "UPDATE fund_expenditures SET status='REJECTED' WHERE expenditure_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM fund_expenditures";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private FundExpenditure mapFE(ResultSet rs) throws SQLException {
        FundExpenditure fe = new FundExpenditure();
        fe.setExpenditureId(rs.getInt("expenditure_id"));
        fe.setNgoId(rs.getInt("ngo_id"));
        fe.setRequestId(rs.getInt("request_id"));
        fe.setAmount(rs.getDouble("amount"));
        fe.setDescription(rs.getString("description"));
        fe.setProofDocument(rs.getString("proof_document"));
        Date d = rs.getDate("spent_date");
        if (d != null) fe.setSpentDate(d.toLocalDate());
        fe.setStatus(rs.getString("status"));
        return fe;
    }
}
