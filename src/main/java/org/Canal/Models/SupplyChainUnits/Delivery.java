package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Utils.LockeStatus;

import java.util.ArrayList;

public class Delivery extends Objex {

    private String salesOrder;
    private String purchaseOrder; //PO delivery is a result of
    private String expectedDelivery; //When it should arrive, could differ from PO
    private String destination; //Location ID
    private Area destinationArea; //
    private Bin destinationDoor; //
    private LockeStatus status; //Status of this Delivery
    private Truck truck;
    private ArrayList<StockLine> pallets;

    public String getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;
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

    public Bin getDestinationDoor() {
        return destinationDoor;
    }

    public void setDestinationDoor(Bin destinationDoor) {
        this.destinationDoor = destinationDoor;
    }

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public ArrayList<StockLine> getPallets() {
        return pallets;
    }

    public void setPallets(ArrayList<StockLine> pallets) {
        this.pallets = pallets;
    }
}