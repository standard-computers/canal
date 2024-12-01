package org.Canal.Models.SupplyChainUnits;

import org.Canal.Start;
import org.Canal.Utils.Json;
import org.Canal.Utils.LockeStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Area {

    private String id; //Area ID for system reference
    private String location; //Location ID this are belongs to
    private String name; //Area name
    private String widthUOM;
    private String lengthUOM;
    private String heightUOM;
    private String weightUOM;
    private String areaUOM;
    private String volumeUOM;
    private double width;
    private double length;
    private double height;
    private double area;
    private double volume;
    private Map<String, String> properties;
    private ArrayList<Bin> bins = new ArrayList<Bin>();
    private LockeStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setVolumeUOM(String volumUOM) {
        this.volumeUOM = volumUOM;
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
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
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

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public void save() {
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\AREAS\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".areas")) {
                    Area forg = Json.load(file.getPath(), Area.class);
                    if (forg.getId().equals(getId())) {
                        Json.save(file.getPath(), this);
                        break;
                    }
                }
            }
        }
    }
}