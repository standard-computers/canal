package org.Canal.Models.SupplyChainUnits;

import org.Canal.Utils.Constants;

public class StockLine {

    private String objex; //ITS, MTS, or CMPS ID
    private String id;
    private double quantity;
    private String area;
    private String bin;
    private String receipt;

    public StockLine(String objex, String id, double quantity, String area, String bin) {
        this.objex = objex;
        this.id = id;
        this.quantity = quantity;
        this.area = area;
        this.bin = bin;
        this.receipt = Constants.now();
    }

    public String getObjex() {
        return objex;
    }

    public void setObjex(String objex) {
        this.objex = objex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public StockLine(String receipt) {
        this.receipt = receipt;
    }
}
