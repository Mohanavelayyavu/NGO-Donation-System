package com.ngo.dao;

import com.ngo.model.MaterialDonation;
import com.ngo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDonationDAO {

    public int create(MaterialDonation donation) throws Exception {
        String sql = "INSERT INTO material_donations (donor_id, item_description, quantity, donation_date) VALUES (?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, donation.getDonorId());
            ps.setString(2, donation.getItemDescription());
            ps.setInt(3, donation.getQuantity());
            ps.setDate(4, donation.getDonationDate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public MaterialDonation getById(int id) throws Exception {
        String sql = "SELECT md.*, u.name AS donor_name FROM material_donations md " +
                     "JOIN users u ON md.donor_id = u.id WHERE md.material_donation_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<MaterialDonation> getAll() throws Exception {
        List<MaterialDonation> list = new ArrayList<>();
        String sql = "SELECT md.*, u.name AS donor_name FROM material_donations md " +
                     "JOIN users u ON md.donor_id = u.id ORDER BY md.donation_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<MaterialDonation> getByDonor(int donorId) throws Exception {
        List<MaterialDonation> list = new ArrayList<>();
        String sql = "SELECT md.*, u.name AS donor_name FROM material_donations md " +
                     "JOIN users u ON md.donor_id = u.id WHERE md.donor_id = ? ORDER BY md.donation_date DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM material_donations WHERE material_donation_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private MaterialDonation mapRow(ResultSet rs) throws SQLException {
        MaterialDonation d = new MaterialDonation();
        d.setMaterialDonationId(rs.getInt("material_donation_id"));
        d.setDonorId(rs.getInt("donor_id"));
        d.setItemDescription(rs.getString("item_description"));
        d.setQuantity(rs.getInt("quantity"));
        d.setDonationDate(rs.getDate("donation_date"));
        return d;
    }
}
