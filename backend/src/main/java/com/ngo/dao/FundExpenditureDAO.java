package com.ngo.dao;

import com.ngo.model.FundExpenditure;
import com.ngo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FundExpenditureDAO {

    public int create(FundExpenditure exp) throws Exception {
        String sql = "INSERT INTO fund_expenditures (ngo_id, purpose, amount, spent_date, material_donation_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, exp.getNgoId());
            ps.setString(2, exp.getPurpose());
            ps.setDouble(3, exp.getAmount());
            ps.setDate(4, exp.getSpentDate());
            if (exp.getMaterialDonationId() != null) {
                ps.setInt(5, exp.getMaterialDonationId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public FundExpenditure getById(int id) throws Exception {
        String sql = "SELECT fe.*, u.name AS ngo_name FROM fund_expenditures fe " +
                     "JOIN users u ON fe.ngo_id = u.id WHERE fe.expenditure_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<FundExpenditure> getAll() throws Exception {
        List<FundExpenditure> list = new ArrayList<>();
        String sql = "SELECT fe.*, u.name AS ngo_name FROM fund_expenditures fe " +
                     "JOIN users u ON fe.ngo_id = u.id ORDER BY fe.spent_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<FundExpenditure> getByNgo(int ngoId) throws Exception {
        List<FundExpenditure> list = new ArrayList<>();
        String sql = "SELECT fe.*, u.name AS ngo_name FROM fund_expenditures fe " +
                     "JOIN users u ON fe.ngo_id = u.id WHERE fe.ngo_id = ? ORDER BY fe.spent_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean update(FundExpenditure exp) throws Exception {
        String sql = "UPDATE fund_expenditures SET purpose=?, amount=?, spent_date=?, material_donation_id=? WHERE expenditure_id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, exp.getPurpose());
            ps.setDouble(2, exp.getAmount());
            ps.setDate(3, exp.getSpentDate());
            if (exp.getMaterialDonationId() != null) {
                ps.setInt(4, exp.getMaterialDonationId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, exp.getExpenditureId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM fund_expenditures WHERE expenditure_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private FundExpenditure mapRow(ResultSet rs) throws SQLException {
        FundExpenditure fe = new FundExpenditure();
        fe.setExpenditureId(rs.getInt("expenditure_id"));
        fe.setNgoId(rs.getInt("ngo_id"));
        fe.setPurpose(rs.getString("purpose"));
        fe.setAmount(rs.getDouble("amount"));
        fe.setSpentDate(rs.getDate("spent_date"));
        int matId = rs.getInt("material_donation_id");
        if (!rs.wasNull()) fe.setMaterialDonationId(matId);
        return fe;
    }
}
