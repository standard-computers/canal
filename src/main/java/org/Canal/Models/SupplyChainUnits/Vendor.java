package org.Canal.Models.SupplyChainUnits;

import org.Canal.Utils.LockeStatus;

public class Vendor {

    private String id;
    private String tie;
    private String name;
    private String line1;
    private String city;
    private String state;
    private String postal;
    private String country;
    private LockeStatus status = LockeStatus.ACTIVE;
    private boolean taxExempt = false;

    public Vendor() {}

    public Vendor(String id, String tie, String name, String line1, String city, String state, String postal, String country, boolean taxExempt) {
        this.id = id;
        this.tie = tie;
        this.name = name;
        this.line1 = line1;
        this.city = city;
        this.state = state;
        this.postal = postal;
        this.country = country;
        this.taxExempt = taxExempt;
    }

    public Vendor(String id, String tie, String name, String line1, String city, String state, String postal, String country) {
        this.id = id;
        this.tie = tie;
        this.name = name;
        this.line1 = line1;
        this.city = city;
        this.state = state;
        this.postal = postal;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTie() {
        return tie;
    }

    public void setTie(String tie) {
        this.tie = tie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }
}