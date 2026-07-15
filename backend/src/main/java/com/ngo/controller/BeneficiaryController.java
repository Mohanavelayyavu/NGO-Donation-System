package com.ngo.controller;

import com.ngo.model.Beneficiary;
import com.ngo.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService service;

    @PostMapping("/beneficiaries")
    public ResponseEntity<?> create(@RequestBody Beneficiary b) {
        boolean ok = service.create(b);
        if (ok) return ResponseEntity.ok(Map.of("message","Beneficiary registered"));
        return ResponseEntity.status(500).body(Map.of("message","Registration failed"));
    }

    @GetMapping("/beneficiaries")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/beneficiaries/pending")
    public ResponseEntity<?> getPending() { return ResponseEntity.ok(service.getPending()); }

    @GetMapping("/beneficiaries/ngo/{ngoId}")
    public ResponseEntity<?> getByNgo(@PathVariable int ngoId) { return ResponseEntity.ok(service.getByNgo(ngoId)); }

    @PutMapping("/beneficiaries/{id}/verify")
    public ResponseEntity<?> verify(@PathVariable int id) {
        boolean ok = service.verify(id);
        if (ok) return ResponseEntity.ok(Map.of("message","Beneficiary verified"));
        return ResponseEntity.status(500).body(Map.of("message","Verification failed"));
    }

    @PutMapping("/beneficiaries/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable int id) {
        boolean ok = service.reject(id);
        if (ok) return ResponseEntity.ok(Map.of("message","Beneficiary rejected"));
        return ResponseEntity.status(500).body(Map.of("message","Rejection failed"));
    }

    @GetMapping("/beneficiaries/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
