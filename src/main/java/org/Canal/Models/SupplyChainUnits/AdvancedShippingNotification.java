package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

/**
 * ASN
 */
public class AdvancedShippingNotification extends Objex {

    private String shipmentId;
    private String carrier; //Carrier ID
    private String expectedDelivery;
    private String trackingNumber;
    private String purchaseOrder; //PO ID
    private double weight;

    public AdvancedShippingNotification() {
        this.type = "ASN";
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}