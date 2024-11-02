package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

public class Component extends Objex {

    private String id;
    private String org;
    private String name;
    private String vendor;
    private String color;
    private String upc;
    private boolean batched;
    private boolean skud;
    private boolean consumable;
    private double price;
    private double width;
    private double length;
    private double height;
    private double weight;
    private double tax;
    private double exciseTax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isBatched() {
        return batched;
    }

    public void setBatched(boolean batched) {
        this.batched = batched;
    }

    public boolean isSkud() {
        return skud;
    }

    public void setSkud(boolean skud) {
        this.skud = skud;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getExciseTax() {
        return exciseTax;
    }

    public void setExciseTax(double exciseTax) {
        this.exciseTax = exciseTax;
    }

    @Override
    public String toString() {
        return id;
    }
}