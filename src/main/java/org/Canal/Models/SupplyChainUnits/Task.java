package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

public class Task extends Objex {

    private String description;
    private String workOrder;
    private String flow;
    private String location;
    private String sourceArea;
    private String sourceBin;
    private String destinationArea;
    private String destinationBin;
    private String employee;
    private String user;
    private String locke; //Destination Locke to listen to
    private String objex; //Associated reference Objex ID
    private String item;
    private double quantity;
    private int employees;
    private boolean goodsIssue;
    private boolean goodsReceipt;
    private double duration;
    private String durationUOM;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSourceArea() {
        return sourceArea;
    }

    public void setSourceArea(String sourceArea) {
        this.sourceArea = sourceArea;
    }

    public String getSourceBin() {
        return sourceBin;
    }

    public void setSourceBin(String sourceBin) {
        this.sourceBin = sourceBin;
    }

    public String getDestinationArea() {
        return destinationArea;
    }

    public void setDestinationArea(String destinationArea) {
        this.destinationArea = destinationArea;
    }

    public String getDestinationBin() {
        return destinationBin;
    }

    public void setDestinationBin(String destinationBin) {
        this.destinationBin = destinationBin;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocke() {
        return locke;
    }

    public void setLocke(String locke) {
        this.locke = locke;
    }

    public String getObjex() {
        return objex;
    }

    public void setObjex(String objex) {
        this.objex = objex;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public boolean doesGoodsIssue() {
        return goodsIssue;
    }

    public void setGoodsIssue(boolean goodsIssue) {
        this.goodsIssue = goodsIssue;
    }

    public boolean doesGoodsReceipt() {
        return goodsReceipt;
    }

    public void setGoodsReceipt(boolean goodsReceipt) {
        this.goodsReceipt = goodsReceipt;
    }

    public boolean isGoodsIssue() {
        return goodsIssue;
    }

    public boolean isGoodsReceipt() {
        return goodsReceipt;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDurationUOM() {
        return durationUOM;
    }

    public void setDurationUOM(String durationUOM) {
        this.durationUOM = durationUOM;
    }
}