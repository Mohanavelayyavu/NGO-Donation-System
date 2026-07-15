package com.ngo.model;

/*
 * Donation Model
 */

public class Donation {

    private int id;
    private int donorId;
    private int requestId;
    private double amount;
    private String donationDate;

    // Default Constructor
    public Donation() {

    }

    // Parameterized Constructor
    public Donation(int id,
                    int donorId,
                    int requestId,
                    double amount,
                    String donationDate) {

        this.id = id;
        this.donorId = donorId;
        this.requestId = requestId;
        this.amount = amount;
        this.donationDate = donationDate;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

}