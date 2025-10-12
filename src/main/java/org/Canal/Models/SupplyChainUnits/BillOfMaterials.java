package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;


/**
 * BOMS
 */
public class BillOfMaterials extends Objex {

    private String location; //If not empty, BOM valid only at this location
    private String item; //Finished Item ID
    private String customer; //Used if customer assigned to manuracturing order
    private ArrayList<StockLine> components;
    private ArrayList<Task> steps;

    public BillOfMaterials() {
        this.type = "BOMS";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ArrayList<StockLine> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<StockLine> components) {
        this.components = components;
    }

    public ArrayList<Task> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Task> steps) {
        this.steps = steps;
    }
}
