package com.ngo.controller;

import com.ngo.model.FundExpenditure;
import com.ngo.service.FundExpenditureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FundExpenditureController {

    @Autowired
    private FundExpenditureService service;

    @PostMapping("/fund-expenditures")
    public ResponseEntity<?> create(@RequestBody FundExpenditure fe) {
        if (fe.getSpentDate() == null) fe.setSpentDate(LocalDate.now());
        boolean ok = service.create(fe);
        if (ok) return ResponseEntity.ok(Map.of("message","Expenditure recorded"));
        return ResponseEntity.status(500).body(Map.of("message","Create failed"));
    }

    @GetMapping("/fund-expenditures")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/fund-expenditures/ngo/{ngoId}")
    public ResponseEntity<?> getByNgo(@PathVariable int ngoId) { return ResponseEntity.ok(service.getByNgo(ngoId)); }

    @GetMapping("/fund-expenditures/pending")
    public ResponseEntity<?> getPending() { return ResponseEntity.ok(service.getPending()); }

    @PutMapping("/fund-expenditures/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable int id) {
        boolean ok = service.approve(id);
        if (ok) return ResponseEntity.ok(Map.of("message","Expenditure approved"));
        return ResponseEntity.status(500).body(Map.of("message","Approval failed"));
    }

    @PutMapping("/fund-expenditures/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable int id) {
        boolean ok = service.reject(id);
        if (ok) return ResponseEntity.ok(Map.of("message","Expenditure rejected"));
        return ResponseEntity.status(500).body(Map.of("message","Rejection failed"));
    }

    @GetMapping("/fund-expenditures/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
