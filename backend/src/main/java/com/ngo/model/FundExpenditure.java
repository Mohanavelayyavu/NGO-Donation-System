package com.ngo.model;

import java.sql.Date;

public class FundExpenditure {
    private int expenditureId;
    private int ngoId;
    private String purpose;
    private double amount;
    private Date spentDate;
    private Integer materialDonationId; // nullable FK to material_donations

    public FundExpenditure() {}

    public FundExpenditure(int ngoId, String purpose, double amount, Date spentDate, Integer materialDonationId) {
        this.ngoId = ngoId;
        this.purpose = purpose;
        this.amount = amount;
        this.spentDate = spentDate;
        this.materialDonationId = materialDonationId;
    }

    public int getExpenditureId() { return expenditureId; }
    public void setExpenditureId(int expenditureId) { this.expenditureId = expenditureId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getSpentDate() { return spentDate; }
    public void setSpentDate(Date spentDate) { this.spentDate = spentDate; }
    public Integer getMaterialDonationId() { return materialDonationId; }
    public void setMaterialDonationId(Integer materialDonationId) { this.materialDonationId = materialDonationId; }
}
