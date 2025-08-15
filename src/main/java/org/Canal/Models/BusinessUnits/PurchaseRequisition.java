package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Json;
import org.Canal.Utils.LockeStatus;

import java.io.File;
import java.util.ArrayList;

public class PurchaseRequisition extends Objex {

    private String number;
    private String supplier;
    private String buyer;
    private double maxSpend;
    private boolean isSingleOrder;
    private String start, end;
    private String notes;
    private ArrayList<OrderLineItem> products = new ArrayList<>();

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<OrderLineItem> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OrderLineItem> products) {
        this.products = products;
    }

    public void save(){
        File md = new File(Start.DIR + "\\.store\\PR\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".pr")) {
                    PurchaseRequisition forg = Json.load(file.getPath(), PurchaseRequisition.class);
                    if (forg.getId().equals(getId())) {
                        Json.save(file.getPath(), this);
                    }
                }
            }
        }
    }
}