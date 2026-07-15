package com.ngo.model;

public class Beneficiary {
    private int id;
    private int userId;
    private int ngoId;
    private String verificationStatus;
    private String contactDetails;
    private String address;
    private String familyDetails;
    private String incomeInfo;

    public Beneficiary() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getContactDetails() { return contactDetails; }
    public void setContactDetails(String contactDetails) { this.contactDetails = contactDetails; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getFamilyDetails() { return familyDetails; }
    public void setFamilyDetails(String familyDetails) { this.familyDetails = familyDetails; }
    public String getIncomeInfo() { return incomeInfo; }
    public void setIncomeInfo(String incomeInfo) { this.incomeInfo = incomeInfo; }
}
