package com.ngo.controller;

import com.ngo.model.Donation;
import com.ngo.service.DonationService;
import com.ngo.service.FundRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationService service;

    @Autowired
    private FundRequestService fundRequestService;

    @PostMapping("/donations")
    public ResponseEntity<?> create(@RequestBody Donation d) {
        if (d.getDonationDate() == null) d.setDonationDate(LocalDate.now());
        boolean ok = service.create(d);
        if (ok) {
            fundRequestService.updateCollectedAmount(d.getRequestId(), d.getAmount());
            return ResponseEntity.ok(Map.of("message","Donation recorded"));
        }
        return ResponseEntity.status(500).body(Map.of("message","Donation failed"));
    }

    @GetMapping("/donations")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/donations/donor/{donorId}")
    public ResponseEntity<?> getByDonor(@PathVariable int donorId) { return ResponseEntity.ok(service.getByDonor(donorId)); }

    @GetMapping("/donations/request/{requestId}")
    public ResponseEntity<?> getByRequest(@PathVariable int requestId) { return ResponseEntity.ok(service.getByRequest(requestId)); }

    @GetMapping("/donations/total")
    public ResponseEntity<?> getTotal() { return ResponseEntity.ok(Map.of("total", service.getTotalAmount())); }

    @GetMapping("/donations/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }

    @GetMapping("/donations/donor/{donorId}/total")
    public ResponseEntity<?> getDonorTotal(@PathVariable int donorId) {
        return ResponseEntity.ok(Map.of("total", service.getTotalAmountByDonor(donorId)));
    }
}
