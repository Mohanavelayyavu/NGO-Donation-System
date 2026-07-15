package com.ngo.dao;

import com.ngo.model.NgoInventory;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * NgoInventoryDAO - All database operations for NgoInventory
 */
public class NgoInventoryDAO {

    public List<NgoInventory> getByNgo(int ngoId) {
        List<NgoInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM ngo_inventory WHERE ngo_id=? ORDER BY inventory_id";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapInv(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<NgoInventory> getAll() {
        List<NgoInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM ngo_inventory ORDER BY ngo_id, inventory_id";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapInv(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addStock(int ngoId, String itemName, String category, int qty) {
        // Check if item exists for this NGO
        String checkSql = "SELECT inventory_id FROM ngo_inventory WHERE ngo_id=? AND item_name=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(checkSql)) {
            ps.setInt(1, ngoId); ps.setString(2, itemName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Update existing
                int invId = rs.getInt("inventory_id");
                String upd = "UPDATE ngo_inventory SET available_qty = available_qty + ? WHERE inventory_id=?";
                try (PreparedStatement up = con.prepareStatement(upd)) {
                    up.setInt(1, qty); up.setInt(2, invId);
                    return up.executeUpdate() > 0;
                }
            } else {
                // Insert new
                String ins = "INSERT INTO ngo_inventory (ngo_id,item_name,category,available_qty) VALUES (?,?,?,?)";
                try (PreparedStatement ins2 = con.prepareStatement(ins)) {
                    ins2.setInt(1, ngoId); ins2.setString(2, itemName);
                    ins2.setString(3, category); ins2.setInt(4, qty);
                    return ins2.executeUpdate() > 0;
                }
            }
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean removeStock(int ngoId, String itemName, int qty) {
        String sql = "UPDATE ngo_inventory SET available_qty = available_qty - ?, distributed_qty = distributed_qty + ? WHERE ngo_id=? AND item_name=? AND available_qty >= ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty); ps.setInt(2, qty);
            ps.setInt(3, ngoId); ps.setString(4, itemName); ps.setInt(5, qty);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM ngo_inventory";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private NgoInventory mapInv(ResultSet rs) throws SQLException {
        NgoInventory inv = new NgoInventory();
        inv.setInventoryId(rs.getInt("inventory_id"));
        inv.setNgoId(rs.getInt("ngo_id"));
        inv.setItemName(rs.getString("item_name"));
        inv.setCategory(rs.getString("category"));
        inv.setAvailableQty(rs.getInt("available_qty"));
        inv.setReservedQty(rs.getInt("reserved_qty"));
        inv.setDistributedQty(rs.getInt("distributed_qty"));
        inv.setDamagedQty(rs.getInt("damaged_qty"));
        return inv;
    }
}
