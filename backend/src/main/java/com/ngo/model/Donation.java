package com.ngo.model;

import java.time.LocalDate;

public class Donation {
    private int donationId;
    private int donorId;
    private int requestId;
    private double amount;
    private String paymentStatus;
    private String donationStatus;
    private LocalDate donationDate;

    public Donation() {}

    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getDonationStatus() { return donationStatus; }
    public void setDonationStatus(String donationStatus) { this.donationStatus = donationStatus; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }
}
