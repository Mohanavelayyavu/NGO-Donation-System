package com.ngo.controller;

import com.ngo.model.FundRequest;
import com.ngo.service.FundRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FundRequestController {

    @Autowired
    private FundRequestService service;

    @PostMapping("/fund-requests")
    public ResponseEntity<?> create(@RequestBody FundRequest fr) {
        boolean ok = service.create(fr);
        if (ok) return ResponseEntity.ok(Map.of("message","Fund request created"));
        return ResponseEntity.status(500).body(Map.of("message","Create failed"));
    }

    @GetMapping("/fund-requests")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/fund-requests/open")
    public ResponseEntity<?> getOpen() { return ResponseEntity.ok(service.getOpen()); }

    @GetMapping("/fund-requests/ngo/{ngoId}")
    public ResponseEntity<?> getByNgo(@PathVariable int ngoId) { return ResponseEntity.ok(service.getByNgo(ngoId)); }

    @GetMapping("/fund-requests/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        FundRequest fr = service.getById(id);
        if (fr != null) return ResponseEntity.ok(fr);
        return ResponseEntity.status(404).body(Map.of("message","Not found"));
    }

    @PutMapping("/fund-requests/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @PathVariable String status) {
        boolean ok = service.updateStatus(id, status);
        if (ok) return ResponseEntity.ok(Map.of("message","Status updated"));
        return ResponseEntity.status(500).body(Map.of("message","Update failed"));
    }

    @GetMapping("/fund-requests/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }

    @GetMapping("/fund-requests/count/{status}")
    public ResponseEntity<?> getCountByStatus(@PathVariable String status) {
        return ResponseEntity.ok(Map.of("count", service.getCountByStatus(status)));
    }
}
