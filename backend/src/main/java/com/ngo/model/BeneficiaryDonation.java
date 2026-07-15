package com.ngo.model;

import java.sql.Date;

public class BeneficiaryDonation {
    private int id;
    private int donorId;
    private int beneficiaryId;
    private String purpose; // fetched via join
    private double amount;
    private Date donationDate;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public int getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(int beneficiaryId) { this.beneficiaryId = beneficiaryId; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDonationDate() { return donationDate; }
    public void setDonationDate(Date donationDate) { this.donationDate = donationDate; }
}
