package org.Canal.Models.SupplyChainUnits;

import org.Canal.UI.Views.Items.Items;

import java.util.ArrayList;

/**
 * A list of available items to someone.
 */
public class Catalog {

    private String id; //Catalog ID
    private String name; //Name of Catalog
    private ArrayList<String> costCenters;
    private ArrayList<String> customers;
    private ArrayList<String> vendors;
    private ArrayList<Items> items = new ArrayList<>();

    public Catalog(String id, String name) {
        this.id = id;
        this.name = name;
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