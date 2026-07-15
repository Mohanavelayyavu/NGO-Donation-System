package com.ngo.model;

/*
 * Donation Request Model
 */

public class DonationRequest {

    private int id;
    private int ngoId;
    private String title;
    private String description;
    private double targetAmount;
    private double collectedAmount;
    private String status;

    // Default Constructor
    public DonationRequest() {

    }

    // Parameterized Constructor
    public DonationRequest(int id,
                           int ngoId,
                           String title,
                           String description,
                           double targetAmount,
                           double collectedAmount,
                           String status) {

        this.id = id;
        this.ngoId = ngoId;
        this.title = title;
        this.description = description;
        this.targetAmount = targetAmount;
        this.collectedAmount = collectedAmount;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNgoId() {
        return ngoId;
    }

    public void setNgoId(int ngoId) {
        this.ngoId = ngoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(double collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}