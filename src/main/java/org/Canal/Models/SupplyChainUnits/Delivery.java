package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

public class Delivery extends Objex {

    private String salesOrder;
    private String purchaseOrder; //PO delivery is a result of
    private String expectedDelivery; //When it should arrive, could differ from PO
    private String origin;
    private String destination; //Location ID
    private String destinationArea; //Area this will arrive to
    private String destinationDoor; //Door this will arrive to
    private String total;
    private String truck; //Truck ID
    private ArrayList<StockLine> pallets = new ArrayList<>();

    @Override
    public void setType(String type) {
        super.setType(type);
    }

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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationArea() {
        return destinationArea;
    }

    public void setDestinationArea(String destinationArea) {
        this.destinationArea = destinationArea;
    }

    public String getDestinationDoor() {
        return destinationDoor;
    }

    public void setDestinationDoor(String destinationDoor) {
        this.destinationDoor = destinationDoor;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public ArrayList<StockLine> getPallets() {
        return pallets;
    }

    public void setPallets(ArrayList<StockLine> pallets) {
        this.pallets = pallets;
    }
}