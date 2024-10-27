package org.Canal.Models.SupplyChainUnits;

import java.util.ArrayList;

/**
 * A list of available items to someone.
 */
public class Catalog {

    private String id, name;
    private String[] costCenters, customers, vendors;
    private ArrayList<Flex> items = new ArrayList<>();

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

    public String[] getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(String[] costCenters) {
        this.costCenters = costCenters;
    }

    public String[] getCustomers() {
        return customers;
    }

    public void setCustomers(String[] customers) {
        this.customers = customers;
    }

    public String[] getVendors() {
        return vendors;
    }

    public void setVendors(String[] vendors) {
        this.vendors = vendors;
    }

    public ArrayList<Flex> getItems() {
        return items;
    }

    public void setItems(ArrayList<Flex> items) {
        this.items = items;
    }
}