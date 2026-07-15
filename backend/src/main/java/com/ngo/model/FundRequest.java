package com.ngo.model;

import java.time.LocalDate;

public class FundRequest {
    private int requestId;
    private int ngoId;
    private String title;
    private String description;
    private String category;
    private double targetAmount;
    private double collectedAmount;
    private String status;
    private LocalDate createdDate;

    public FundRequest() {}

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    public double getCollectedAmount() { return collectedAmount; }
    public void setCollectedAmount(double collectedAmount) { this.collectedAmount = collectedAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
}
