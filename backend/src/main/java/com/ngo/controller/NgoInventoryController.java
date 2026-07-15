package com.ngo.controller;

import com.ngo.model.NgoInventory;
import com.ngo.service.NgoInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class NgoInventoryController {

    @Autowired
    private NgoInventoryService service;

    @GetMapping("/inventory/ngo/{ngoId}")
    public ResponseEntity<?> getByNgo(@PathVariable int ngoId) { return ResponseEntity.ok(service.getByNgo(ngoId)); }

    @GetMapping("/inventory")
    public ResponseEntity<?> getAll() { return ResponseEntity.ok(service.getAll()); }

    @PostMapping("/inventory/add")
    public ResponseEntity<?> addStock(@RequestBody Map<String,String> body) {
        int ngoId = Integer.parseInt(body.get("ngoId"));
        String itemName = body.get("itemName");
        String category = body.get("category");
        int qty = Integer.parseInt(body.get("qty"));
        boolean ok = service.addStock(ngoId, itemName, category, qty);
        if (ok) return ResponseEntity.ok(Map.of("message","Stock added"));
        return ResponseEntity.status(500).body(Map.of("message","Add failed"));
    }

    @PutMapping("/inventory/remove")
    public ResponseEntity<?> removeStock(@RequestBody Map<String,String> body) {
        int ngoId = Integer.parseInt(body.get("ngoId"));
        String itemName = body.get("itemName");
        int qty = Integer.parseInt(body.get("qty"));
        boolean ok = service.removeStock(ngoId, itemName, qty);
        if (ok) return ResponseEntity.ok(Map.of("message","Stock removed"));
        return ResponseEntity.status(500).body(Map.of("message","Remove failed - insufficient stock"));
    }

    @GetMapping("/inventory/count")
    public ResponseEntity<?> getCount() { return ResponseEntity.ok(Map.of("count", service.getCount())); }
}
