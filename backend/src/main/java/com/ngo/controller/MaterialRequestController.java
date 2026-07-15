package com.ngo.controller;

import com.ngo.model.MaterialRequest;
import com.ngo.service.MaterialRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService service;

    @PostMapping("/material-requests")
    public ResponseEntity<?> create(@RequestBody MaterialRequest mr) {
        if (mr.getCreatedDate() == null) mr.setCreatedDate(LocalDate.now());
        boolean ok = service.create(mr);
        if (ok) return ResponseEntity.ok(Map.of("message","Material request created"));
        return ResponseEntity.status(500).body(Map.of("message","Create failed"));
    }

    @GetMapping("/material-requests")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/material-requests/open")
    public ResponseEntity<?> getOpen() { return ResponseEntity.ok(service.getOpen()); }

    @GetMapping("/material-requests/ngo/{ngoId}")
    public ResponseEntity<?> getByNgo(@PathVariable int ngoId) { return ResponseEntity.ok(service.getByNgo(ngoId)); }

    @PutMapping("/material-requests/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @PathVariable String status) {
        boolean ok = service.updateStatus(id, status);
        if (ok) return ResponseEntity.ok(Map.of("message","Status updated"));
        return ResponseEntity.status(500).body(Map.of("message","Update failed"));
    }

    @GetMapping("/material-requests/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
