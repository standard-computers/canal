package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

import java.util.Map;

/**
 * AREAS
 * Areas represent a physical space within a Location Objex.
 * Areas are a standalone Objex.
 * Bins (BNS) belong to AREAS which means they are saved within that MDB colleciton.
 */
public class Area extends Objex {

    //General Properties
    private String location = ""; //Location ID this are belongs to

    //Dimensional
    private double width;
    private String widthUOM;
    private double length;
    private String lengthUOM;
    private double height;
    private String heightUOM;
    private Map<String, String> properties;

    //Controls
    private boolean allowsInventory = true;
    private boolean allowsProduction = false;
    private boolean allowsSales = true;
    private boolean allowsPurchasing = true;

    public Area() {
        this.type = "AREAS";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWidthUOM() {
        return widthUOM;
    }

    public void setWidthUOM(String widthUOM) {
        this.widthUOM = widthUOM;
    }

    public String getHeightUOM() {
        return heightUOM;
    }

    public void setHeightUOM(String heightUOM) {
        this.heightUOM = heightUOM;
    }

    public String getLengthUOM() {
        return lengthUOM;
    }

    public void setLengthUOM(String lengthUOM) {
        this.lengthUOM = lengthUOM;
    }

    public String getAreaUOM() {
        return widthUOM + "2";
    }

    public String getVolumeUOM() {
        return heightUOM + "3";
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

    public double getArea() {
        return width * length;
    }

    public double getVolume() {
        return width * length * height;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public boolean allowsInventory() {
        return allowsInventory;
    }

    public void setAllowsInventory(boolean allowsInventory) {
        this.allowsInventory = allowsInventory;
    }

    public boolean allowsProduction() {
        return allowsProduction;
    }

    public void setAllowsProduction(boolean allowsProduction) {
        this.allowsProduction = allowsProduction;
    }

    public boolean allowsSales() {
        return allowsSales;
    }

    public void setAllowsSales(boolean allowsSales) {
        this.allowsSales = allowsSales;
    }

    public boolean allowsPurchasing() {
        return allowsPurchasing;
    }

    public void setAllowsPurchasing(boolean allowsPurchasing) {
        this.allowsPurchasing = allowsPurchasing;
    }
}