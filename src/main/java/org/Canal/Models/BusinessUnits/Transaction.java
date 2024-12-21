package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

public class Transaction extends Objex {

    private String locke; //Canal transaction was commited with
    private String objex; //Locke Code for Objex type
    private String location;
    private String reference;
    private double amount;
    private String committed; //Timestamp transaction committed
    private String settled; //Timestamp transaction settled

    public String getLocke() {
        return locke;
    }

    public void setLocke(String locke) {
        this.locke = locke;
    }

    public String getObjex() {
        return objex;
    }

    public void setObjex(String objex) {
        this.objex = objex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCommitted() {
        return committed;
    }

    public void setCommitted(String committed) {
        this.committed = committed;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }
}