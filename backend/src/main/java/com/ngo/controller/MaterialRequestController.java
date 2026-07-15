package com.ngo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ngo.model.MaterialRequest;
import com.ngo.service.MaterialRequestService;

/*
 * Material Request Controller
 */
@RestController
@CrossOrigin(origins = "*")
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService service;

    // NGO creates a material request
    @PostMapping("/material-requests")
    public String create(@RequestBody MaterialRequest req) {
        int id = service.create(req);
        return id > 0 ? "Material request created with ID: " + id : "Failed to create material request";
    }

    // Get all material requests (admin)
    @GetMapping("/material-requests")
    public List<MaterialRequest> getAll() {
        return service.getAll();
    }

    // Get only OPEN requests (for donors to see)
    @GetMapping("/material-requests/open")
    public List<MaterialRequest> getOpen() {
        return service.getOpen();
    }

    // Get by NGO
    @GetMapping("/material-requests/ngo/{ngoId}")
    public List<MaterialRequest> getByNgo(@PathVariable int ngoId) {
        return service.getByNgo(ngoId);
    }

    // Get by Beneficiary
    @GetMapping("/material-requests/beneficiary/{beneficiaryId}")
    public List<MaterialRequest> getByBeneficiary(@PathVariable int beneficiaryId) {
        return service.getByBeneficiary(beneficiaryId);
    }

    // Donor fulfills a material request (adds quantity and logs donor)
    @PutMapping("/material-requests/{id}/fulfill/{qty}/{donorId}/{itemName}")
    public String fulfill(@PathVariable int id, 
                          @PathVariable int qty, 
                          @PathVariable int donorId, 
                          @PathVariable String itemName) {
        boolean ok = service.fulfill(id, qty, donorId, itemName);
        return ok ? "Thank you! Fulfillment recorded." : "Failed to record fulfillment";
    }

    // Admin/NGO updates status (OPEN / CLOSED)
    @PutMapping("/material-requests/{id}/status/{status}")
    public String updateStatus(@PathVariable int id, @PathVariable String status) {
        boolean ok = service.updateStatus(id, status);
        return ok ? "Status updated to " + status : "Update failed";
    }

    // Delete (admin)
    @DeleteMapping("/material-requests/{id}")
    public String delete(@PathVariable int id) {
        return service.delete(id) ? "Deleted" : "Delete failed";
    }
}
