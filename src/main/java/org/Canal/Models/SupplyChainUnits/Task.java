package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

public class Task extends Objex {

    private String type;
    private String description;
    private String workOrder;
    private String flow;
    private String location;
    private String employee;
    private String user;
    private String locke; //Destination Locke to listen to
    private String objex; //Associated reference Objex ID

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
}