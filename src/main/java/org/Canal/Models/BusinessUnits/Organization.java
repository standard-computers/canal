package org.Canal.Models.BusinessUnits;

public class Organization {

    private String id;
    private String name;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postal;
    private String country;
    private String ein;
    private boolean taxExempt;

    public Organization() {}

    public Organization(String id, String name, String line1, String line2, String city, String state, String postal, String country, boolean taxExempt) {
        this.id = id;
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

    public void setId(String id) {
        this.id = id;
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

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
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

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    @Override
    public String toString() {
        return this.id;
    }
}