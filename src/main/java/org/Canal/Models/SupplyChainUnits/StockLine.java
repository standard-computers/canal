package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Utils.Constants;
import org.Canal.Utils.LockeStatus;

public class StockLine extends Objex {

    private String objex; //Items, MTS, or CMPS ID
    private String hu;
    private String sku;
    private double quantity;
    private String unitOfMeasure;
    private String batch;
    private String area;
    private String bin;
    private String receipt;

    public StockLine(String objex, String id, double quantity, String area, String bin) {
        this.objex = objex;
        this.hu = Constants.generateId(10);
        this.id = id;
        this.quantity = quantity;
        this.area = area;
        this.bin = bin;
        this.receipt = Constants.now();
        this.status = LockeStatus.UNRESTRICTED;
    }

    public String getObjex() {
        return objex;
    }

    public void setObjex(String objex) {
        this.objex = objex;
    }

    public String getHu() {
        return hu;
    }

    public void setHu(String hu) {
        this.hu = hu;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
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

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
