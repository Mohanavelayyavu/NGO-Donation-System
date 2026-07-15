package com.ngo.model;

import java.sql.Date;

/*
 * Material Request Model
 * NGOs raise requests for specific physical items (e.g. blankets, food bags)
 */
public class MaterialRequest {

    private int requestId;
    private int ngoId;
    private String ngoName;         // joined from users
    private int beneficiaryId;
    private String beneficiaryName; // joined from users
    private String itemName;
    private String description;
    private int quantityNeeded;
    private int quantityReceived;   // updated as donors fulfill
    private String status;          // OPEN, FULFILLED, CLOSED
    private Date createdDate;

    public int getRequestId()           { return requestId; }
    public void setRequestId(int v)     { this.requestId = v; }

    public int getNgoId()               { return ngoId; }
    public void setNgoId(int v)         { this.ngoId = v; }

    public String getNgoName()          { return ngoName; }
    public void setNgoName(String v)    { this.ngoName = v; }

    public int getBeneficiaryId()           { return beneficiaryId; }
    public void setBeneficiaryId(int v)     { this.beneficiaryId = v; }

    public String getBeneficiaryName()      { return beneficiaryName; }
    public void setBeneficiaryName(String v){ this.beneficiaryName = v; }

    public String getItemName()         { return itemName; }
    public void setItemName(String v)   { this.itemName = v; }

    public String getDescription()      { return description; }
    public void setDescription(String v){ this.description = v; }

    public int getQuantityNeeded()      { return quantityNeeded; }
    public void setQuantityNeeded(int v){ this.quantityNeeded = v; }

    public int getQuantityReceived()    { return quantityReceived; }
    public void setQuantityReceived(int v){ this.quantityReceived = v; }

    public String getStatus()           { return status; }
    public void setStatus(String v)     { this.status = v; }

    public Date getCreatedDate()        { return createdDate; }
    public void setCreatedDate(Date v)  { this.createdDate = v; }
}
