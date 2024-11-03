package org.Canal.Models.BusinessUnits;

import org.Canal.Start;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Json;
import org.Canal.Utils.LockeStatus;

import java.io.File;

public class PurchaseRequisition {

    private String id;
    private String created;
    private String name;
    private String owner;
    private String number;
    private String supplier;
    private String buyer;
    private double maxSpend;
    private boolean isSingleOrder;
    private String start, end;
    private LockeStatus status;
    private String notes;

    public PurchaseRequisition(String id, String name, String owner, String supplier, String buyer, String number, double maxSpend, String start, String end, String notes) {
        this.id = id;
        this.created = Constants.now();
        this.name = name;
        this.owner = owner;
        this.supplier = supplier;
        this.buyer = buyer;
        this.number = number;
        this.maxSpend = maxSpend;
        this.start = start;
        this.end = end;
        this.status = LockeStatus.NEW;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void save(){
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\PR\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".pr")) {
                    PurchaseRequisition forg = Json.load(file.getPath(), PurchaseRequisition.class);
                    if (forg.getId().equals(getId())) {
                        Json.save(file.getPath(), this);
                    }
                    break;
                }
            }
        }
    }
}