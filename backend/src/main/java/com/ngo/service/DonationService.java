package com.ngo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.ngo.dao.DonationDAO;
import com.ngo.model.Donation;

/*
 * Donation Service
 */

@Service
public class DonationService {

    private DonationDAO dao = new DonationDAO();
    private com.ngo.dao.DonationRequestDAO requestDAO = new com.ngo.dao.DonationRequestDAO();

    // Donate

    public boolean donate(Donation donation) {

        boolean status = dao.donate(donation);
        if (status) {
            requestDAO.updateCollectedAmount(donation.getRequestId(), donation.getAmount());
        }
        return status;

    }

    // View Donations

    public ArrayList<Donation> getAllDonations() {

        return dao.getAllDonations();

    }

    // Donation By Id

    public Donation getDonationById(int id) {

        return dao.getDonationById(id);

    }

    // Donor Donations

    public ArrayList<Donation> getDonationsByDonor(int donorId) {

        return dao.getDonationsByDonor(donorId);

    }

    // Request Donations

    public ArrayList<Donation> getDonationsByRequest(int requestId) {

        return dao.getDonationsByRequest(requestId);

    }

    // Total Amount

    public double getTotalDonationAmount() {

        return dao.getTotalDonationAmount();

    }

    // Request Total

    public double getDonationAmountByRequest(int requestId) {

        return dao.getDonationAmountByRequest(requestId);

    }

    // Delete

    public boolean deleteDonation(int id) {

        return dao.deleteDonation(id);

    }

}