package com.ngo.controller;

import com.ngo.model.BeneficiaryAllocation;
import com.ngo.service.BeneficiaryAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class BeneficiaryAllocationController {

    @Autowired
    private BeneficiaryAllocationService service;

    @PostMapping("/beneficiary-allocations")
    public ResponseEntity<?> create(@RequestBody BeneficiaryAllocation ba) {
        if (ba.getAllocationDate() == null) ba.setAllocationDate(LocalDate.now());
        boolean ok = service.create(ba);
        if (ok) return ResponseEntity.ok(Map.of("message","Allocation created"));
        return ResponseEntity.status(500).body(Map.of("message","Create failed"));
    }

    @GetMapping("/beneficiary-allocations")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/beneficiary-allocations/ngo/{ngoId}")
    public ResponseEntity<?> getByNgo(@PathVariable int ngoId) { return ResponseEntity.ok(service.getByNgo(ngoId)); }

    @PutMapping("/beneficiary-allocations/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable int id) {
        boolean ok = service.confirmReceipt(id);
        if (ok) return ResponseEntity.ok(Map.of("message","Receipt confirmed"));
        return ResponseEntity.status(500).body(Map.of("message","Confirm failed"));
    }

    @GetMapping("/beneficiary-allocations/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
