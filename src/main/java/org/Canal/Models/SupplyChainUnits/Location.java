package org.Canal.Models.SupplyChainUnits;

public class Location {

    private String id, tie, name, line1, city, state, postal, country, status = "Active";
    private boolean taxExempt = false;

    public Location(String id, String tie, String name, String line1, String city, String state, String postal, String country, boolean taxExempt) {
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

    public Location(String id, String tie, String name, String line1, String city, String state, String postal, String country) {
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

    public String getTie() {
        return tie;
    }

    public String getName() {
        return name;
    }

    public String getLine1() {
        return line1;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostal() {
        return postal;
    }

    public String getCountry() {
        return country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }
}