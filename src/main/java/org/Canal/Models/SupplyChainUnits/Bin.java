package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.Objex;
import org.Canal.Utils.Engine;

import java.util.ArrayList;

/**
 * BNS
 * This Objex belongs to AREAS
 * For inventory and storage of items.
 */
public class Bin extends Objex {

    private String area; //Area ID this bin is in
    private ArrayList<String> allowed_items;
    private int[] coordinates = new int[3];
    private double width;
    private String widthUOM;
    private double length;
    private String lengthUOM;
    private double height;
    private String heightUOM;
    private double weight;
    private String weightUOM;
    private boolean auto_replenish; //If this bin is on a flow, do not replenish if false
    private boolean fixed; //Only one item allowed
    private boolean putaway;
    private boolean picking;
    private boolean goodsissue;
    private boolean goodsreceipt;
    private boolean holdsStock;

    public Bin() {
        this.type = "BNS";
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

    public double getAreaValue() {
        return width * height;
    }

    public String getAreaUOM() {
        return widthUOM + "2";
    }

    public double getVolume() {
        return width * length * height;
    }

    public String getVolumeUOM() {
        return heightUOM + "3";
    }

    public boolean pickingEnabled() {
        return picking;
    }

    public void pickingEnabled(boolean picking) {
        this.picking = picking;
    }

    public boolean putawayEnabled() {
        return putaway;
    }

    public void putawayEnabled(boolean putaway) {
        this.putaway = putaway;
    }

    public boolean doesGI() {
        return goodsissue;
    }

    public void doesGI(boolean goodsissue) {
        this.goodsissue = goodsissue;
    }

    public boolean doesGR() {
        return goodsreceipt;
    }

    public void doesGR(boolean goodsreceipt) {
        this.goodsreceipt = goodsreceipt;
    }

    public boolean holdsStock() {
        return holdsStock;
    }

    public void holdsStock(boolean holdsStock) {
        this.holdsStock = holdsStock;
    }

    public boolean hasInventory() {
        Area a = Engine.getArea(this.area);
        Inventory i = Engine.getInventory(a.getLocation());
        for (StockLine sl : i.getStockLines()) {
            if (sl.getBin().equals(this.id)) {
                return true;
            }
        }
        return false;
    }
}