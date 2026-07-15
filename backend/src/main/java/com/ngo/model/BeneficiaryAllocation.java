package com.ngo.model;

import java.time.LocalDate;

public class BeneficiaryAllocation {
    private int allocationId;
    private int ngoId;
    private int beneficiaryRequestId;
    private String allocationType;
    private double allocatedAmount;
    private int allocatedQuantity;
    private String distributionStatus;
    private String otpPin;
    private LocalDate allocationDate;

    public BeneficiaryAllocation() {}

    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public int getBeneficiaryRequestId() { return beneficiaryRequestId; }
    public void setBeneficiaryRequestId(int beneficiaryRequestId) { this.beneficiaryRequestId = beneficiaryRequestId; }
    public String getAllocationType() { return allocationType; }
    public void setAllocationType(String allocationType) { this.allocationType = allocationType; }
    public double getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(double allocatedAmount) { this.allocatedAmount = allocatedAmount; }
    public int getAllocatedQuantity() { return allocatedQuantity; }
    public void setAllocatedQuantity(int allocatedQuantity) { this.allocatedQuantity = allocatedQuantity; }
    public String getDistributionStatus() { return distributionStatus; }
    public void setDistributionStatus(String distributionStatus) { this.distributionStatus = distributionStatus; }
    public String getOtpPin() { return otpPin; }
    public void setOtpPin(String otpPin) { this.otpPin = otpPin; }
    public LocalDate getAllocationDate() { return allocationDate; }
    public void setAllocationDate(LocalDate allocationDate) { this.allocationDate = allocationDate; }
}
