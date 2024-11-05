package org.Canal.Models.BusinessUnits;

import org.Canal.Start;
import org.Canal.Utils.Json;
import org.Canal.Utils.LockeStatus;

import java.io.File;
import java.util.ArrayList;

public class PurchaseOrder {

    private String orderId; //Order ID
    private String owner = "UNKNOWN"; //ID of User who is creating order
    private String orderedOn; //Timestamp this was ordered on
    private String expectedDelivery; //When this should arrive to ship to
    private String purchaseRequisition; //Purchase Requisition ID
    private String billTo; //Location ID
    private String shipTo; //Location ID
    private String soldTo; //Location ID
    private String transaction;
    private String customer;
    private String vendor;
    private ArrayList<OrderLineItem> items = new ArrayList<>();
    private double netValue, taxRate, taxAmount, total;
    private LockeStatus status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(String orderedOn) {
        this.orderedOn = orderedOn;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public String getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(String purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public ArrayList<OrderLineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderLineItem> items) {
        this.items = items;
    }

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void save(){
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\ORDS\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".ords")) {
                    PurchaseOrder forg = Json.load(file.getPath(), PurchaseOrder.class);
                    if (forg.getOrderId().equals(getOrderId())) {
                        Json.save(file.getPath(), this);
                    }
                    break;
                }
            }
        }
    }
}