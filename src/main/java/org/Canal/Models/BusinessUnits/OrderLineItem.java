package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

/**
 * ORDS/L
 * This class represents a set of items
 * at a set purchase price.
 */
public class OrderLineItem extends Objex {

    private double quantity;
    private double onHand;
    private double price;
    private double total;

    public OrderLineItem(String name, String id, double quantity, double price, double total) {
        this.type = "ORDS/L";
        super.name = name;
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public OrderLineItem(String itemId, String name, double quantity, double onHand) {
        this.type = "ORDS/L";
        this.id = itemId;
        this.name = name;
        this.quantity = quantity;
        this.onHand = onHand;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getOnHand() {
        return onHand;
    }

    public void setOnHand(double onHand) {
        this.onHand = onHand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}