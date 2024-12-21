package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

public class DebitMemo extends Objex {

    private String customer;
    private String reference;
    private double quantity;
    private double value;
    private ArrayList<Objex> products;
    private String note;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Objex> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Objex> products) {
        this.products = products;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
