package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * MOS
 */
public class ManufacturingOrder extends Objex {

    private String item; //Item ID
    private String operator; //Employee ID
    private String startDate; //Manufacture Start Date
    private String endDate; //Manufacture End Date
    private String reference; //Reference string if valid
    private String bom; //Reference string if valid
    private String customer; //Customer Location ID
    private String area; //Workcenter Area ID
    private String workcenter; //Workcenter Bin ID
    private String location; //Manufacturing Location ID
    private double quantity;
    private ArrayList<StockLine> consumptions = new ArrayList<>(); //Consumptions for the MO
    private ArrayList<MaterialMovement> movements = new ArrayList<>(); //Product movements
    private boolean qualityCheck; //Requires Quality Check
    private String notes;

    public ManufacturingOrder() {
        this.type = "MOS";
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBom() {
        return bom;
    }

    public void setBom(String bom) {
        this.bom = bom;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWorkcenter() {
        return workcenter;
    }

    public void setWorkcenter(String workcenter) {
        this.workcenter = workcenter;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ArrayList<StockLine> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(ArrayList<StockLine> consumptions) {
        this.consumptions = consumptions;
    }

    public ArrayList<MaterialMovement> getMovements() {
        return movements;
    }

    public void setMovements(ArrayList<MaterialMovement> movements) {
        this.movements = movements;
    }

    public boolean isQualityCheck() {
        return qualityCheck;
    }

    public void setQualityCheck(boolean qualityCheck) {
        this.qualityCheck = qualityCheck;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
