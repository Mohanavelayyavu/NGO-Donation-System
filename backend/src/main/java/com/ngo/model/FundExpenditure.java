package com.ngo.model;

import java.time.LocalDate;

public class FundExpenditure {
    private int expenditureId;
    private int ngoId;
    private int requestId;
    private double amount;
    private String description;
    private String proofDocument;
    private LocalDate spentDate;
    private String status;

    public FundExpenditure() {}

    public int getExpenditureId() { return expenditureId; }
    public void setExpenditureId(int expenditureId) { this.expenditureId = expenditureId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProofDocument() { return proofDocument; }
    public void setProofDocument(String proofDocument) { this.proofDocument = proofDocument; }
    public LocalDate getSpentDate() { return spentDate; }
    public void setSpentDate(LocalDate spentDate) { this.spentDate = spentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
