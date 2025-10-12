package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

/**
 * BTH
 */
public class Batch extends Objex {

    private String lot;
    private String vendor; //Vendor ID
    private String manufactured;
    private String bestByDate;

    public Batch() {
        this.type = "BTH";
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getManufactured() {
        return manufactured;
    }

    public void setManufactured(String manufactured) {
        this.manufactured = manufactured;
    }

    public String getBestByDate() {
        return bestByDate;
    }

    public void setBestByDate(String bestByDate) {
        this.bestByDate = bestByDate;
    }
}
