package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * ITS
 *
 */
public class Item extends Objex {

    //General (Info) Properties
    private String org;
    private String vendor = "";
    private String color = "";
    private double price;
    private String upc;
    private String vendorNumber;

    //Controls Properties
    private boolean batched;
    private boolean rentable;
    private boolean skud;
    private boolean consumable;
    private boolean virtual;
    private boolean allowSales;
    private boolean allowPurchasing;
    private boolean keepInventory;

    //Dimensional Properties
    private double baseQuantity;
    private String packagingUnit;
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
    private double shelfLife;
    private String link;
    private ArrayList<StockLine> components = new ArrayList<>();
    private ArrayList<Object[]> uoms = new ArrayList<>();
    private ArrayList<Object[]> packaging = new ArrayList<>();

    //Planning Variables
    private double leadTime = 1.0;
    private double transporationTime = 1.0;
    private double manufacturingTime = 1.0;

    public Item() {
        this.type = "ITS";
    }

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

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
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

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean allowSales() {
        return allowSales;
    }

    public void allowSales(boolean allowSales) {
        this.allowSales = allowSales;
    }

    public boolean allowPurchasing() {
        return allowPurchasing;
    }

    public void allowPurchasing(boolean allowPurchasing) {
        this.allowPurchasing = allowPurchasing;
    }

    public boolean keepInventory() {
        return keepInventory;
    }

    public void keepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public double getPrice() {
        return price;
    }

    public double getBasePrice() {
        return price / baseQuantity;
    }

    public double getBasePrice(double quantity) {
        return (price / baseQuantity) * quantity;
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

    public double getVolume() {
        return width * length * height;
    }

    public String getVolumeUOM() {
        return heightUOM + 3;
    }

    public double getSurfaceArea() {
        return 2 * (width * length + width * height + length * height);
    }

    public String getSurfaceAreaUOM() {
        return widthUOM + 2;
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

    public double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<StockLine> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<StockLine> components) {
        this.components = components;
    }

    public void addComponent(StockLine component) {
        this.components.add(component);
    }

    public ArrayList<Object[]> getUoms() {
        return uoms;
    }

    public void setUoms(ArrayList<Object[]> uoms) {
        this.uoms = uoms;
    }

    public ArrayList<Object[]> getPackaging() {
        return packaging;
    }

    public void setPackaging(ArrayList<Object[]> packaging) {
        this.packaging = packaging;
    }

    public double getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(double leadTime) {
        this.leadTime = leadTime;
    }

    public double getTransporationTime() {
        return transporationTime;
    }

    public void setTransporationTime(double transporationTime) {
        this.transporationTime = transporationTime;
    }

    public double getManufacturingTime() {
        return manufacturingTime;
    }

    public void setManufacturingTime(double manufacturingTime) {
        this.manufacturingTime = manufacturingTime;
    }

    @Override
    public String toString() {
        return super.getId();
    }
}