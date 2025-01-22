package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

/**
 * Assets help manage physical assets like vehicles, computers, etc.
 *
 */
public class Asset extends Objex {

    private String vendor;
    private String party; //Employee ID
    private String partyEntitledToDispose;
    private String description; //Asset (item) description
    private String item; //Associated Item ID if any
    private double value; //Value of item or price paid
    private double tax; //Tax paid on item
    private double annualDepreciation; //As percent (5% = 0.05)

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPartyEntitledToDispose() {
        return partyEntitledToDispose;
    }

    public void setPartyEntitledToDispose(String partyEntitledToDispose) {
        this.partyEntitledToDispose = partyEntitledToDispose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getAnnualDepreciation() {
        return annualDepreciation;
    }

    public void setAnnualDepreciation(double annualDepreciation) {
        this.annualDepreciation = annualDepreciation;
    }
}