package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Json;

import java.io.File;
import java.util.ArrayList;

public class Delivery extends Objex {

    private String type;
    private String salesOrder;
    private String purchaseOrder; //PO delivery is a result of
    private String expectedDelivery; //When it should arrive, could differ from PO
    private String origin;
    private String destination; //Location ID
    private Area destinationArea; //Area this will arrive to
    private Bin destinationDoor; //Door this will arrive to
    private String total;
    private Truck truck;
    private ArrayList<StockLine> pallets = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public void save(){
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\TRANS\\" + type + "\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith("." + type.toLowerCase())) {
                    Ledger fl = Json.load(file.getPath(), Ledger.class);
                    if (fl.getId().equals(id)) {
                        Json.save(file.getPath(), this);
                    }
                }
            }
        }
    }
}