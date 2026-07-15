package com.ngo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ngo.model.FundExpenditure;
import com.ngo.service.FundExpenditureService;

/*
 * Fund Expenditure Controller
 */

@RestController
@CrossOrigin(origins = "*")
public class FundExpenditureController {

    @Autowired
    private FundExpenditureService service;

    // Create expenditure
    @PostMapping("/expenditures")
    public String create(@RequestBody FundExpenditure exp) {
        int id = service.create(exp);
        if (id > 0) {
            return "Expenditure recorded with ID: " + id;
        }
        return "Failed to record expenditure";
    }

    // Get all expenditures
    @GetMapping("/expenditures")
    public List<FundExpenditure> getAll() {
        return service.getAll();
    }

    // Get by ID
    @GetMapping("/expenditures/{id}")
    public FundExpenditure getById(@PathVariable int id) {
        return service.getById(id);
    }

    // Get by NGO
    @GetMapping("/expenditures/ngo/{ngoId}")
    public List<FundExpenditure> getByNgo(@PathVariable int ngoId) {
        return service.getByNgo(ngoId);
    }

    // Update expenditure
    @PutMapping("/expenditures/{id}")
    public String update(@PathVariable int id, @RequestBody FundExpenditure exp) {
        exp.setExpenditureId(id);
        boolean status = service.update(exp);
        if (status) {
            return "Expenditure updated";
        }
        return "Update failed";
    }

    // Delete expenditure
    @DeleteMapping("/expenditures/{id}")
    public String delete(@PathVariable int id) {
        boolean status = service.delete(id);
        if (status) {
            return "Expenditure deleted";
        }
        return "Delete failed";
    }
}
