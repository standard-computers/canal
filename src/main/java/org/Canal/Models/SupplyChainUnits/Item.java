package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

public class Item extends Objex {

    private String org;
    private String vendor;
    private String color;
    private String upc;
    private String uom;
    private String widthUOM;
    private String lengthUOM;
    private String heightUOM;
    private String packagingUnit;
    private boolean batched;
    private boolean rentable;
    private boolean skud;
    private boolean consumable;
    private double price;
    private double width;
    private double length;
    private double height;
    private double weight;
    private double tax;
    private double exciseTax;

    public Item() {
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getPackagingUnit() {
        return packagingUnit;
    }

    public void setPackagingUnit(String packagingUnit) {
        this.packagingUnit = packagingUnit;
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

    public boolean isRentable() {
        return rentable;
    }

    public void setRentable(boolean rentable) {
        this.rentable = rentable;
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
        return super.getId();
    }
}