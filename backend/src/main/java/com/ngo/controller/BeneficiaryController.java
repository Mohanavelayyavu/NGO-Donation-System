package com.ngo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.model.Beneficiary;
import com.ngo.service.BeneficiaryService;

@RestController
@RequestMapping("/beneficiaries")
@CrossOrigin(origins = "*")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    // Apply for Help
    @PostMapping
    public boolean addApplication(@RequestBody Beneficiary beneficiary) {

        return beneficiaryService.addApplication(beneficiary);

    }

    // Get All Applications
    @GetMapping
    public List<Beneficiary> getAllApplications() {

        return beneficiaryService.getAllApplications();

    }

    // Get Application By ID
    @GetMapping("/{id}")
    public Beneficiary getApplicationById(@PathVariable int id) {

        return beneficiaryService.getApplicationById(id);

    }

    // Update Status (Approve / Reject)
    @PutMapping("/{id}/{status}")
    public boolean updateStatus(@PathVariable int id,
                                @PathVariable String status) {
        return beneficiaryService.updateStatus(id, status);
    }

    // Donate to Beneficiary
    @PostMapping("/{id}/donate/{donorId}/{amount}")
    public String donateToBeneficiary(@PathVariable int id, 
                                      @PathVariable int donorId, 
                                      @PathVariable double amount) {
        boolean ok = beneficiaryService.donateToBeneficiary(id, donorId, amount);
        return ok ? "Donation successful! Thank you for helping." : "Donation failed. Please try again.";
    }

    // Get donations by donor
    @GetMapping("/donations/donor/{donorId}")
    public List<com.ngo.model.BeneficiaryDonation> getDonationsByDonor(@PathVariable int donorId) {
        return beneficiaryService.getDonationsByDonor(donorId);
    }
}