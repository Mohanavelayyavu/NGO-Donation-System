package com.ngo.controller;

import com.ngo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AdminStatsController {

    @Autowired private UserService userService;
    @Autowired private FundRequestService fundRequestService;
    @Autowired private DonationService donationService;
    @Autowired private MaterialDonationService materialDonationService;
    @Autowired private BeneficiaryService beneficiaryService;
    @Autowired private FundExpenditureService fundExpenditureService;

    @GetMapping("/admin/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalCount());
        stats.put("totalNGOs", userService.getCountByRole("NGO"));
        stats.put("totalDonors", userService.getCountByRole("DONOR"));
        stats.put("totalBeneficiaries", userService.getCountByRole("BENEFICIARY"));
        stats.put("activeRequests", fundRequestService.getCountByStatus("OPEN"));
        stats.put("completedRequests", fundRequestService.getCountByStatus("COMPLETED"));
        stats.put("totalFunds", donationService.getTotalAmount());
        stats.put("totalMaterials", materialDonationService.getCount());
        stats.put("pendingVerifications", beneficiaryService.getPending().size());
        stats.put("pendingUtilizationReports", fundExpenditureService.getPending().size());
        return ResponseEntity.ok(stats);
    }
}
