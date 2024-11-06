package org.Canal.Models.SupplyChainUnits;

import java.util.ArrayList;

/**
 * For inventory and storage of items.
 */
public class Bin {

    private String id;
    private String name;
    private String area; //Area ID this bin is in
    private boolean auto_replenish; //If this bin is on a flow, do not replenish if false
    private boolean fixed; //Only one item allowed
    private ArrayList<String> allowed_items;
    private int[] coordinates = new int[3];
    private int min; //Min quantity of items before replenishment
    private int max; //Quantity of items it may contain
    private String widthUOM;
    private String lengthUOM;
    private String heightUOM;
    private String weightUOM;
    private String areaUOM;
    private String volumeUOM;
    private double width;
    private double length;
    private double height;
    private double volume;

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isAuto_replenish() {
        return auto_replenish;
    }

    public void setAuto_replenish(boolean auto_replenish) {
        this.auto_replenish = auto_replenish;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public ArrayList<String> getAllowed_items() {
        return allowed_items;
    }

    public void setAllowed_items(ArrayList<String> allowed_items) {
        this.allowed_items = allowed_items;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getWidthUOM() {
        return widthUOM;
    }

    public void setWidthUOM(String widthUOM) {
        this.widthUOM = widthUOM;
    }

    public String getLengthUOM() {
        return lengthUOM;
    }

    public void setLengthUOM(String lengthUOM) {
        this.lengthUOM = lengthUOM;
    }

    public String getHeightUOM() {
        return heightUOM;
    }

    public void setHeightUOM(String heightUOM) {
        this.heightUOM = heightUOM;
    }

    public String getWeightUOM() {
        return weightUOM;
    }

    public void setWeightUOM(String weightUOM) {
        this.weightUOM = weightUOM;
    }

    public String getAreaUOM() {
        return areaUOM;
    }

    public void setAreaUOM(String areaUOM) {
        this.areaUOM = areaUOM;
    }

    public String getVolumeUOM() {
        return volumeUOM;
    }

    public void setVolumeUOM(String volumeUOM) {
        this.volumeUOM = volumeUOM;
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

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}