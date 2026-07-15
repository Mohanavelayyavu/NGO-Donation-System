package com.ngo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngo.dao.MaterialDonationDAO;
import com.ngo.model.MaterialDonation;

/*
 * Material Donation Service
 */

@Service
public class MaterialDonationService {

    private MaterialDonationDAO dao = new MaterialDonationDAO();

    // Create material donation
    public int create(MaterialDonation donation) {
        try {
            return dao.create(donation);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Get all material donations
    public List<MaterialDonation> getAll() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    // Get by ID
    public MaterialDonation getById(int id) {
        try {
            return dao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get by donor
    public List<MaterialDonation> getByDonor(int donorId) {
        try {
            return dao.getByDonor(donorId);
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    // Delete material donation
    public boolean delete(int id) {
        try {
            return dao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
