package org.Canal.Models.SupplyChainUnits;

import org.Canal.UI.Views.Items.Items;

import java.util.ArrayList;

/**
 * A list of available items to someone.
 */
public class Catalog {

    private String id; //Catalog ID
    private String name; //Name of Catalog
    private String description;
    private String period;
    private String validFrom;
    private String validTo;
    private ArrayList<String> costCenters;
    private ArrayList<String> customers;
    private ArrayList<String> vendors;
    private ArrayList<Items> items = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public ArrayList<String> getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(ArrayList<String> costCenters) {
        this.costCenters = costCenters;
    }

    public ArrayList<String> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<String> customers) {
        this.customers = customers;
    }

    public ArrayList<String> getVendors() {
        return vendors;
    }

    public void setVendors(ArrayList<String> vendors) {
        this.vendors = vendors;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }
}