package com.ngo.controller;

import com.ngo.model.BeneficiaryRequest;
import com.ngo.service.BeneficiaryRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class BeneficiaryRequestController {

    @Autowired
    private BeneficiaryRequestService service;

    @PostMapping("/beneficiary-requests")
    public ResponseEntity<?> create(@RequestBody BeneficiaryRequest br) {
        if (br.getCreatedDate() == null) br.setCreatedDate(LocalDate.now());
        boolean ok = service.create(br);
        if (ok) return ResponseEntity.ok(Map.of("message","Request submitted"));
        return ResponseEntity.status(500).body(Map.of("message","Submit failed"));
    }

    @GetMapping("/beneficiary-requests")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/beneficiary-requests/beneficiary/{id}")
    public ResponseEntity<?> getByBeneficiary(@PathVariable int id) { return ResponseEntity.ok(service.getByBeneficiary(id)); }

    @GetMapping("/beneficiary-requests/pending")
    public ResponseEntity<?> getPending() { return ResponseEntity.ok(service.getPending()); }

    @PutMapping("/beneficiary-requests/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable int id) {
        boolean ok = service.approve(id);
        if (ok) return ResponseEntity.ok(Map.of("message","Request approved"));
        return ResponseEntity.status(500).body(Map.of("message","Approval failed"));
    }

    @GetMapping("/beneficiary-requests/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
