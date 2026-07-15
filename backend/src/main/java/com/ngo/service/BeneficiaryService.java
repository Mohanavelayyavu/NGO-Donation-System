package com.ngo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngo.dao.BeneficiaryDAO;
import com.ngo.model.Beneficiary;

/*
 * Beneficiary Service Class
 * Handles business logic
 */

@Service
public class BeneficiaryService {

    private BeneficiaryDAO dao = new BeneficiaryDAO();

    // Add Beneficiary Application
    public boolean addApplication(Beneficiary beneficiary) {

        return dao.addApplication(beneficiary);

    }

    // Get All Applications
    public List<Beneficiary> getAllApplications() {

        return dao.getAllApplications();

    }

    // Get Application By ID
    public Beneficiary getApplicationById(int id) {

        return dao.getApplicationById(id);

    }

    // Update Status
    public boolean updateStatus(int id, String status) {
        return dao.updateStatus(id, status);
    }

    // Donate to Beneficiary
    public boolean donateToBeneficiary(int beneficiaryId, int donorId, double amount) {
        return dao.donateToBeneficiary(beneficiaryId, donorId, amount);
    }

    // Get donations by donor
    public List<com.ngo.model.BeneficiaryDonation> getDonationsByDonor(int donorId) {
        return dao.getDonationsByDonor(donorId);
    }
}