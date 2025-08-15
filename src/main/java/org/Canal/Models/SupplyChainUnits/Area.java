package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Json;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Area extends Objex {

    private String location; //Location ID this are belongs to
    private double width;
    private String widthUOM;
    private double length;
    private String lengthUOM;
    private double height;
    private String heightUOM;
    private Map<String, String> properties;
    private ArrayList<Bin> bins = new ArrayList<>();

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

    public ArrayList<Bin> getBins() {
        return bins;
    }

    public void setBins(ArrayList<Bin> bins) {
        this.bins = bins;
    }

    public void addBin(Bin newBin) {
        bins.add(newBin);
    }

    public void save() {
        File md = new File(Start.DIR + "\\.store\\AREAS\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".areas")) {
                    Area forg = Json.load(file.getPath(), Area.class);
                    if (forg.getId().equals(getId())) {
                        Json.save(file.getPath(), this);
                    }
                }
            }
        }
    }
}