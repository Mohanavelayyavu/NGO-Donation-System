package com.ngo.model;

import java.sql.Date;

public class MaterialDonation {
    private int materialDonationId;
    private int donorId;
    private String itemDescription;
    private int quantity;
    private Date donationDate;

    public MaterialDonation() {}

    public MaterialDonation(int donorId, String itemDescription, int quantity, Date donationDate) {
        this.donorId = donorId;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.donationDate = donationDate;
    }

    public int getMaterialDonationId() { return materialDonationId; }
    public void setMaterialDonationId(int materialDonationId) { this.materialDonationId = materialDonationId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Date getDonationDate() { return donationDate; }
    public void setDonationDate(Date donationDate) { this.donationDate = donationDate; }
}
