package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * ORDS/PR
 */
public class PurchaseRequisition extends Objex {

    private String number;
    private String supplier;
    private String buyer;
    private double maxSpend;
    private boolean isSingleOrder;
    private String start;
    private String end;
    private ArrayList<OrderLineItem> items = new ArrayList<>();

    public PurchaseRequisition() {
        this.type = "ORDS/PR";
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getMaxSpend() {
        return maxSpend;
    }

    public void setMaxSpend(double maxSpend) {
        this.maxSpend = maxSpend;
    }

    public boolean isSingleOrder() {
        return isSingleOrder;
    }

    public void setSingleOrder(boolean singleOrder) {
        isSingleOrder = singleOrder;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public ArrayList<OrderLineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderLineItem> items) {
        this.items = items;
    }
}