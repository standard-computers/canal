package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Json;
import org.Canal.Utils.LockeStatus;

import java.io.File;
import java.util.ArrayList;

public class Location extends Objex {

    private String type;
    private String organization;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postal;
    private String country;
    private boolean taxExempt = false;
    private String email;
    private String phone;
    private double area; //Square area of warehouse
    private String areaUOM;
    private ArrayList<Department> departments = new ArrayList<>();
    private LockeStatus status = LockeStatus.ACTIVE;

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

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
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

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getAreaUOM() {
        return areaUOM;
    }

    public void setAreaUOM(String areaUOM) {
        this.areaUOM = areaUOM;
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

    public void save(){
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\" + type + "\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".orgs")) {
                    Location forg = Json.load(file.getPath(), Location.class);
                    if (forg.getId().equals(this.getId())) {
                        Json.save(file.getPath(), this);
                        break;
                    }
                }
            }
        }
    }
}