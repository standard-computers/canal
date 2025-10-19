package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.Objex;

import java.util.ArrayList;

public class Location extends Objex {

    private String organization;

    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postal;
    private String country;


    private boolean taxExempt = false;
    private String ein = "UNKNOWN";
    private String email = "UNKNOWN";
    private String phone = "UNKNOWN";
    private double width;
    private String widthUOM;
    private double length;
    private String lengthUOM;
    private double height;
    private String heightUOM;
    private ArrayList<Department> departments = new ArrayList<>();

    //Controls
    private boolean allowsInventory = true;
    private boolean allowsProduction = false;
    private boolean allowsSales = true;
    private boolean allowsPurchasing = true;

    //TODO
    private boolean reservations = true; //Consider Stock Reservations
    private boolean deficits = false; //Allow Stock Deficits
    private boolean doProduction = true; //If item and location can produce, make MO
    private boolean backorder = true; //Block order creation if false and no stock for item
    private ArrayList<String> backup; //Array of Location IDs
    private int cadence = 86400; //Every X Seconds AutoMake a Plan and execute it for this location
    private ArrayList<String> suppliers = new ArrayList<>();


    @Override
    public void setType(String type) {
        super.setType(type);
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public double getArea() {
        return width * length;
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

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public void addDepartment(Department department) {
        this.departments.add(department);
    }

    public Department getDepartment(String id) {
        for (Department department : departments) {
            if (department.getId().equals(id)) {
                return department;
            }
        }
        return null;
    }

    public void removeDepartment(Department department) {
        this.departments.remove(department);
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

    public boolean isReservations() {
        return reservations;
    }

    public void setReservations(boolean reservations) {
        this.reservations = reservations;
    }

    public boolean isDeficits() {
        return deficits;
    }

    public void setDeficits(boolean deficits) {
        this.deficits = deficits;
    }

    public boolean isDoProduction() {
        return doProduction;
    }

    public void setDoProduction(boolean doProduction) {
        this.doProduction = doProduction;
    }

    public boolean isBackorder() {
        return backorder;
    }

    public void setBackorder(boolean backorder) {
        this.backorder = backorder;
    }

    public ArrayList<String> getBackup() {
        return backup;
    }

    public void setBackup(ArrayList<String> backup) {
        this.backup = backup;
    }

    public int getCadence() {
        return cadence;
    }

    public void setCadence(int cadence) {
        this.cadence = cadence;
    }

    public ArrayList<String> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<String> suppliers) {
        this.suppliers = suppliers;
    }
}