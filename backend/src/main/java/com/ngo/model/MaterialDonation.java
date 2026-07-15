package com.ngo.model;

import java.time.LocalDate;

public class MaterialDonation {
    private int donationId;
    private int donorId;
    private int requestId;
    private String itemName;
    private int quantity;
    private int damagedQuantity;
    private String courierName;
    private String trackingNumber;
    private LocalDate expectedDeliveryDate;
    private String deliveryStatus;
    private String receiptStatus;
    private LocalDate donationDate;

    public MaterialDonation() {}

    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(int damagedQuantity) { this.damagedQuantity = damagedQuantity; }
    public String getCourierName() { return courierName; }
    public void setCourierName(String courierName) { this.courierName = courierName; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public String getReceiptStatus() { return receiptStatus; }
    public void setReceiptStatus(String receiptStatus) { this.receiptStatus = receiptStatus; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }
}
