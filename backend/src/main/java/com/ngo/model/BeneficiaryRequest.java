package com.ngo.model;

import java.time.LocalDate;

public class BeneficiaryRequest {
    private int requestId;
    private int beneficiaryId;
    private String assistanceCategory;
    private String itemName;
    private double requestedAmount;
    private int requestedQuantity;
    private String reason;
    private String urgency;
    private String status;
    private LocalDate createdDate;

    public BeneficiaryRequest() {}

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(int beneficiaryId) { this.beneficiaryId = beneficiaryId; }
    public String getAssistanceCategory() { return assistanceCategory; }
    public void setAssistanceCategory(String assistanceCategory) { this.assistanceCategory = assistanceCategory; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(double requestedAmount) { this.requestedAmount = requestedAmount; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public void setRequestedQuantity(int requestedQuantity) { this.requestedQuantity = requestedQuantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
}
