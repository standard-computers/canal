package org.Canal.Models.SupplyChainUnits;

public class Organization {

    private String id, tie, name, line1, line2, city, state, postal, country;
    private boolean taxExempt;

    public Organization(String id, String tie, String name, String line1, String line2, String city, String state, String postal, String country, boolean taxExempt) {
        this.id = id;
        this.tie = tie;
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.postal = postal;
        this.country = country;
        this.taxExempt = taxExempt;
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

    public String getLine2() {
        return line2;
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

    public boolean isTaxExempt() {
        return taxExempt;
    }

    @Override
    public String toString() {
        return this.id;
    }
}