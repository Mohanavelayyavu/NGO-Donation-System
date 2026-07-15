package com.ngo.controller;

import com.ngo.model.MaterialDonation;
import com.ngo.service.MaterialDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MaterialDonationController {

    @Autowired
    private MaterialDonationService service;

    @PostMapping("/material-donations")
    public ResponseEntity<?> pledge(@RequestBody MaterialDonation md) {
        if (md.getDonationDate() == null) md.setDonationDate(LocalDate.now());
        boolean ok = service.pledge(md);
        if (ok) return ResponseEntity.ok(Map.of("message","Material donation pledged"));
        return ResponseEntity.status(500).body(Map.of("message","Pledge failed"));
    }

    @GetMapping("/material-donations")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/material-donations/donor/{donorId}")
    public ResponseEntity<?> getByDonor(@PathVariable int donorId) { return ResponseEntity.ok(service.getByDonor(donorId)); }

    @GetMapping("/material-donations/request/{requestId}")
    public ResponseEntity<?> getByRequest(@PathVariable int requestId) { return ResponseEntity.ok(service.getByRequest(requestId)); }

    @PutMapping("/material-donations/{id}/logistics")
    public ResponseEntity<?> updateLogistics(@PathVariable int id, @RequestBody Map<String,String> body) {
        boolean ok = service.updateLogistics(id, body.get("courierName"), body.get("trackingNumber"), body.get("expectedDeliveryDate"));
        if (ok) return ResponseEntity.ok(Map.of("message","Logistics updated"));
        return ResponseEntity.status(500).body(Map.of("message","Update failed"));
    }

    @PutMapping("/material-donations/{id}/delivered")
    public ResponseEntity<?> markDelivered(@PathVariable int id, @RequestBody Map<String,String> body) {
        int damaged = body.containsKey("damagedQuantity") ? Integer.parseInt(body.get("damagedQuantity")) : 0;
        boolean ok = service.markDelivered(id, damaged);
        if (ok) return ResponseEntity.ok(Map.of("message","Marked as delivered"));
        return ResponseEntity.status(500).body(Map.of("message","Update failed"));
    }

    @GetMapping("/material-donations/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
