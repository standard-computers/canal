package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

/**
 * RTS
 */
public class Rate extends Objex {

    private String description;
    private boolean percent; //If value should be used as percent (i.e. 0.05)
    private double value; //Value of rate
    private String objex; //Objex type this rate is for.
    private String reference; //Objex ID this rate applies to
    private boolean tax; //This rate is a tax

    public Rate() {
        this.type = "RTS";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPercent() {
        return percent;
    }

    public void setPercent(boolean percent) {
        this.percent = percent;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getObjex() {
        return objex;
    }

    public void setObjex(String objex) {
        this.objex = objex;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isTax() {
        return tax;
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }
}
