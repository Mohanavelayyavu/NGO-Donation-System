package com.ngo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.ngo.dao.DonationRequestDAO;
import com.ngo.model.DonationRequest;

/*
 * Donation Request Service
 */

@Service
public class DonationRequestService {

    private DonationRequestDAO dao = new DonationRequestDAO();

    // Add Request

    public boolean addRequest(DonationRequest request) {

        return dao.addRequest(request);

    }

    // View All Requests

    public ArrayList<DonationRequest> getAllRequests() {

        return dao.getAllRequests();

    }

    // View Request By Id

    public DonationRequest getRequestById(int id) {

        return dao.getRequestById(id);

    }

    // NGO Requests

    public ArrayList<DonationRequest> getRequestsByNgo(int ngoId) {

        return dao.getRequestsByNgo(ngoId);

    }

    // Update Request

    public boolean updateRequest(DonationRequest request) {

        return dao.updateRequest(request);

    }

    // Update Collected Amount

    public boolean updateCollectedAmount(int requestId,double amount) {

        return dao.updateCollectedAmount(requestId, amount);

    }

    // Delete Request

    public boolean deleteRequest(int id) {

        return dao.deleteRequest(id);

    }

    // Active Requests

    public ArrayList<DonationRequest> getActiveRequests() {

        return dao.getActiveRequests();

    }

}