package com.ngo.model;

public class NgoInventory {
    private int inventoryId;
    private int ngoId;
    private String itemName;
    private String category;
    private int availableQty;
    private int reservedQty;
    private int distributedQty;
    private int damagedQty;

    public NgoInventory() {}

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getAvailableQty() { return availableQty; }
    public void setAvailableQty(int availableQty) { this.availableQty = availableQty; }
    public int getReservedQty() { return reservedQty; }
    public void setReservedQty(int reservedQty) { this.reservedQty = reservedQty; }
    public int getDistributedQty() { return distributedQty; }
    public void setDistributedQty(int distributedQty) { this.distributedQty = distributedQty; }
    public int getDamagedQty() { return damagedQty; }
    public void setDamagedQty(int damagedQty) { this.damagedQty = damagedQty; }
}
