package com.ngo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.model.Donation;
import com.ngo.service.DonationService;

/*
 * Donation Controller
 */

@RestController
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationService service;

    // Donate

    @PostMapping("/donate")
    public String donate(@RequestBody Donation donation) {

        boolean status = service.donate(donation);

        if (status) {
            return "Donation Successful";
        }

        return "Donation Failed";

    }

    // View All Donations

    @GetMapping("/donations")
    public ArrayList<Donation> getDonations() {

        return service.getAllDonations();

    }

    // Donation By Id

    @GetMapping("/donations/{id}")
    public Donation getDonation(@PathVariable int id) {

        return service.getDonationById(id);

    }

    // Donations By Donor

    @GetMapping("/donations/donor/{id}")
    public ArrayList<Donation> getDonorDonations(@PathVariable int id) {

        return service.getDonationsByDonor(id);

    }

    // Donations By Request

    @GetMapping("/donations/request/{id}")
    public ArrayList<Donation> getRequestDonations(@PathVariable int id) {

        return service.getDonationsByRequest(id);

    }

    // Total Donation Amount

    @GetMapping("/donations/total")
    public double getTotalDonationAmount() {

        return service.getTotalDonationAmount();

    }

    // Total Donation Amount For One Request

    @GetMapping("/donations/total/{requestId}")
    public double getRequestTotal(@PathVariable int requestId) {

        return service.getDonationAmountByRequest(requestId);

    }

    // Delete Donation

    @DeleteMapping("/donations/{id}")
    public String deleteDonation(@PathVariable int id) {

        boolean status = service.deleteDonation(id);

        if (status) {
            return "Donation Deleted";
        }

        return "Delete Failed";

    }

}