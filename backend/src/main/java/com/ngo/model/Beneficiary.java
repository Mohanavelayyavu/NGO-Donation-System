package com.ngo.model;

/*
 * Beneficiary Model Class
 * Represents a beneficiary application
 */

public class Beneficiary {

    private int id;
    private int userId;
    private String purpose;
    private String description;
    private double requiredAmount;
    private double collectedAmount;
    private String status;

    // Default Constructor
    public Beneficiary() {
    }

    // Parameterized Constructor
    public Beneficiary(int id, int userId, String purpose, String description,
                       double requiredAmount, double collectedAmount, String status) {
        this.id = id;
        this.userId = userId;
        this.purpose = purpose;
        this.description = description;
        this.requiredAmount = requiredAmount;
        this.collectedAmount = collectedAmount;
        this.status = status;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(double requiredAmount) {
        this.requiredAmount = requiredAmount;
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