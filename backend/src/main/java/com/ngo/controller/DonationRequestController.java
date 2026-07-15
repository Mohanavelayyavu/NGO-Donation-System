package com.ngo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.model.DonationRequest;
import com.ngo.service.DonationRequestService;

/*
 * Donation Request Controller
 */

@RestController
@CrossOrigin(origins = "*")
public class DonationRequestController {

    @Autowired
    private DonationRequestService service;

    // Add Request

    @PostMapping("/requests")
    public String addRequest(@RequestBody DonationRequest request) {

        boolean status = service.addRequest(request);

        if(status) {
            return "Request Added Successfully";
        }

        return "Failed";

    }

    // View All Requests

    @GetMapping("/requests")
    public ArrayList<DonationRequest> getRequests() {

        return service.getAllRequests();

    }

    // Request By Id

    @GetMapping("/requests/{id}")
    public DonationRequest getRequest(@PathVariable int id) {

        return service.getRequestById(id);

    }

    // NGO Requests

    @GetMapping("/requests/ngo/{id}")
    public ArrayList<DonationRequest> getNgoRequests(@PathVariable int id) {

        return service.getRequestsByNgo(id);

    }

    // Update Request

    @PutMapping("/requests")
    public String updateRequest(@RequestBody DonationRequest request) {

        boolean status = service.updateRequest(request);

        if(status) {
            return "Request Updated";
        }

        return "Update Failed";

    }

    // Delete Request

    @DeleteMapping("/requests/{id}")
    public String deleteRequest(@PathVariable int id) {

        boolean status = service.deleteRequest(id);

        if(status) {
            return "Request Deleted";
        }

        return "Delete Failed";

    }

}