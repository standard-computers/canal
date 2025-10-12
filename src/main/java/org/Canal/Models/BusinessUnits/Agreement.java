package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * AGS
 */
public class Agreement extends Objex {

    private String starts;
    private String ends;
    private String paymentTerms;
    private ArrayList<String> locations = new ArrayList<>(); //Locations belonging to this agreement

    public Agreement() {
        this.type = "AGS";
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }
}
