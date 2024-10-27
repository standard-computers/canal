package org.Canal.Models.SupplyChainUnits;

import org.Canal.Utils.LockeType;

public class OutboundDelivery {

    private String id;
    private String purchaseOrder; //PO delivery is a result of
    private String expectedDelivery; //When it should arrive, could differ from PO
    private String destination; //Location ID
    private Area destinationArea; //
    private Door destinationDoor; //
    private LockeType status; //Status of this Delivery

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Area getDestinationArea() {
        return destinationArea;
    }

    public void setDestinationArea(Area destinationArea) {
        this.destinationArea = destinationArea;
    }

    public Door getDestinationDoor() {
        return destinationDoor;
    }

    public void setDestinationDoor(Door destinationDoor) {
        this.destinationDoor = destinationDoor;
    }
}