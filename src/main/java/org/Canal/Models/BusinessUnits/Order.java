package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * ORDS/[PO || SO]
 * Type is set within associated create Lockes
 */
public class Order extends Objex {

    private String orderId; //Order ID
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
    private ArrayList<Rate> rates = new ArrayList<>();
    private double netValue;
    private double taxAmount;
    private double total;

    @Override
    public void setType(String type){
        super.setType(type);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public double getTotalItems() {
        double total = 0;
        for (OrderLineItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<Rate> rates) {
        this.rates = rates;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
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
}