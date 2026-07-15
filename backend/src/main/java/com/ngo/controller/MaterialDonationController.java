package com.ngo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ngo.model.MaterialDonation;
import com.ngo.service.MaterialDonationService;

/*
 * Material Donation Controller
 */

@RestController
@CrossOrigin(origins = "*")
public class MaterialDonationController {

    @Autowired
    private MaterialDonationService service;

    // Create material donation
    @PostMapping("/material-donations")
    public String create(@RequestBody MaterialDonation donation) {
        int id = service.create(donation);
        if (id > 0) {
            return "Material donation recorded with ID: " + id;
        }
        return "Failed to record material donation";
    }

    // Get all material donations
    @GetMapping("/material-donations")
    public List<MaterialDonation> getAll() {
        return service.getAll();
    }

    // Get by ID
    @GetMapping("/material-donations/{id}")
    public MaterialDonation getById(@PathVariable int id) {
        return service.getById(id);
    }

    // Get by donor
    @GetMapping("/material-donations/donor/{donorId}")
    public List<MaterialDonation> getByDonor(@PathVariable int donorId) {
        return service.getByDonor(donorId);
    }

    // Delete material donation
    @DeleteMapping("/material-donations/{id}")
    public String delete(@PathVariable int id) {
        boolean status = service.delete(id);
        if (status) {
            return "Material donation deleted";
        }
        return "Delete failed";
    }
}
