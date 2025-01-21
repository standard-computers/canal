package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

public class OrderLineItem extends Objex {

    private String org;
    private String vendor;
    private String color;
    private String upc;
    private double baseQuantity;
    private String packagingUnit;
    private boolean batched;
    private boolean rentable;
    private boolean skud;
    private boolean consumable;
    private double price;
    private double width;
    private String widthUOM;
    private double length;
    private String lengthUOM;
    private double height;
    private String heightUOM;
    private double weight;
    private String weightUOM;
    private double initialVolume;
    private String initialVolumeUOM;
    private double tax;
    private double exciseTax;
    private ArrayList<OrderLineItem> components = new ArrayList<>();

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
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

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public double getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(double baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public String getPackagingUnit() {
        return packagingUnit;
    }

    public void setPackagingUnit(String packagingUnit) {
        this.packagingUnit = packagingUnit;
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

    public String getWidthUOM() {
        return widthUOM;
    }

    public void setWidthUOM(String widthUOM) {
        this.widthUOM = widthUOM;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getLengthUOM() {
        return lengthUOM;
    }

    public void setLengthUOM(String lengthUOM) {
        this.lengthUOM = lengthUOM;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getHeightUOM() {
        return heightUOM;
    }

    public void setHeightUOM(String heightUOM) {
        this.heightUOM = heightUOM;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUOM() {
        return weightUOM;
    }

    public void setWeightUOM(String weightUOM) {
        this.weightUOM = weightUOM;
    }

    public double getInitialVolume() {
        return initialVolume;
    }

    public void setInitialVolume(double initialVolume) {
        this.initialVolume = initialVolume;
    }

    public String getInitialVolumeUOM() {
        return initialVolumeUOM;
    }

    public void setInitialVolumeUOM(String initialVolumeUOM) {
        this.initialVolumeUOM = initialVolumeUOM;
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

    public ArrayList<OrderLineItem> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<OrderLineItem> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return super.getId();
    }
}