package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

public class Truck extends Objex {

    private String number = "N/A"; //Actual Truck Number
    private String carrier; //Carrier ID
    private String driver = ""; //Person ID
    private String licensePlate = "";
    private String vin = "";
    private String year = "";
    private String make = "";
    private String model = "";
    private String delivery = ""; //Delivery ID (IDO or ODO)
    private double weight;
    private String weightUOM;
    private double maxWeight;
    private String maxWeightUOM;

    public Truck() {
        this.type = "TRANS/TRCKS";
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
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

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getMaxWeightUOM() {
        return maxWeightUOM;
    }

    public void setMaxWeightUOM(String maxWeightUOM) {
        this.maxWeightUOM = maxWeightUOM;
    }
}