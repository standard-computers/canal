package org.Canal.Models.BusinessUnits;

import org.Canal.Utils.LockeStatus;
import java.util.ArrayList;

public class GoodsReceipt {

    public String id; //GR ID
    public String purchaseOrder; //PO this is tied to
    public String received; //Timestamp of when received
    public String receiver; //User ID
    public String location; //Location receiving in at
    public ArrayList<OrderLineItem> items; //Items received and qty received
    private LockeStatus status; //Status of this receival

    public GoodsReceipt(String id, String purchaseOrder, String received, String receiver, String location, ArrayList<OrderLineItem> items, LockeStatus status) {
        this.id = id;
        this.purchaseOrder = purchaseOrder;
        this.received = received;
        this.receiver = receiver;
        this.location = location;
        this.items = items;
        this.status = status;
    }

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

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<OrderLineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderLineItem> items) {
        this.items = items;
    }

    public double getTotalItems(){
        double total = 0;
        for(OrderLineItem item : items){
            total += item.getQuantity();
        }
        return total;
    }

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }
}