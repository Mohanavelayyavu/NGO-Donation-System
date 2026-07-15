package com.ngo.controller;

import com.ngo.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class AuditLogController {

    @Autowired
    private AuditLogService service;

    @GetMapping("/audit-logs")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/audit-logs/recent")
    public ResponseEntity<?> getRecent(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getRecent(limit));
    }
}
