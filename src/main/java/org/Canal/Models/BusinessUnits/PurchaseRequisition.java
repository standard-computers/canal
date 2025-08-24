package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import java.io.File;
import java.util.ArrayList;

public class PurchaseRequisition extends Objex {

    private String number;
    private String supplier;
    private String buyer;
    private double maxSpend;
    private boolean isSingleOrder;
    private String start;
    private String end;
    private String notes;
    private ArrayList<OrderLineItem> items = new ArrayList<>();

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

    public ArrayList<OrderLineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderLineItem> items) {
        this.items = items;
    }

    public void save() {
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File md = new File(Start.DIR + "\\.store\\ORDS\\PR\\");
            File[] mdf = md.listFiles();
            if (mdf != null) {
                for (File file : mdf) {
                    if (file.getPath().endsWith(".pr")) {
                        PurchaseRequisition forg = Pipe.load(file.getPath(), PurchaseRequisition.class);
                        if (forg.getId().equals(getId())) {
                            Pipe.export(file.getPath(), this);
                        }
                    }
                }
            }
        } else {
            Pipe.save("ORDS/PR", this);
        }
    }
}