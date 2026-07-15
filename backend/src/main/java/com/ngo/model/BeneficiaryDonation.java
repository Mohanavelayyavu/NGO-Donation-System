package com.ngo.model;

import java.time.LocalDate;

public class BeneficiaryDonation {
    private int donationId;
    private int donorId;
    private int beneficiaryId;
    private double amount;
    private String purpose;
    private LocalDate donationDate;

    public BeneficiaryDonation() {}

    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public int getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(int beneficiaryId) { this.beneficiaryId = beneficiaryId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }
}
