package com.ngo.dao;

import com.ngo.model.BeneficiaryAllocation;
import com.ngo.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * BeneficiaryAllocationDAO - All database operations for BeneficiaryAllocation
 */
public class BeneficiaryAllocationDAO {

    public boolean create(BeneficiaryAllocation ba) {
        String sql = "INSERT INTO beneficiary_allocations (ngo_id,beneficiary_request_id,allocation_type,allocated_amount,allocated_quantity,distribution_status,otp_pin,allocation_date) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ba.getNgoId()); ps.setInt(2, ba.getBeneficiaryRequestId());
            ps.setString(3, ba.getAllocationType()); ps.setDouble(4, ba.getAllocatedAmount());
            ps.setInt(5, ba.getAllocatedQuantity());
            ps.setString(6, ba.getDistributionStatus() != null ? ba.getDistributionStatus() : "ASSIGNED");
            ps.setString(7, ba.getOtpPin()); ps.setObject(8, ba.getAllocationDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<BeneficiaryAllocation> getAll() {
        List<BeneficiaryAllocation> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiary_allocations ORDER BY allocation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBA(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<BeneficiaryAllocation> getByNgo(int ngoId) {
        List<BeneficiaryAllocation> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiary_allocations WHERE ngo_id=? ORDER BY allocation_id DESC";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ngoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBA(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<BeneficiaryAllocation> getByBeneficiaryRequest(int requestId) {
        List<BeneficiaryAllocation> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiary_allocations WHERE beneficiary_request_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBA(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean confirmReceipt(int id) {
        String sql = "UPDATE beneficiary_allocations SET distribution_status='DELIVERED' WHERE allocation_id=?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM beneficiary_allocations";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private BeneficiaryAllocation mapBA(ResultSet rs) throws SQLException {
        BeneficiaryAllocation ba = new BeneficiaryAllocation();
        ba.setAllocationId(rs.getInt("allocation_id"));
        ba.setNgoId(rs.getInt("ngo_id"));
        ba.setBeneficiaryRequestId(rs.getInt("beneficiary_request_id"));
        ba.setAllocationType(rs.getString("allocation_type"));
        ba.setAllocatedAmount(rs.getDouble("allocated_amount"));
        ba.setAllocatedQuantity(rs.getInt("allocated_quantity"));
        ba.setDistributionStatus(rs.getString("distribution_status"));
        ba.setOtpPin(rs.getString("otp_pin"));
        Date d = rs.getDate("allocation_date");
        if (d != null) ba.setAllocationDate(d.toLocalDate());
        return ba;
    }
}
