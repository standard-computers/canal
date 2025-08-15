package org.Canal.Models.HumanResources;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Json;

import java.io.File;

public class Employee extends Objex {

    private String org; //Org ID
    private String firstName; //Nickname or Reference Name of Employee (first + last)
    private String middleName;
    private String lastName;
    private String location; //Location ID if any
    private String supervisor; //Employee ID of Supervisor
    private String position;
    private String startDate; //Employee employment date
    private String endDate; //Employee termination date
    private String createDate;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postal;
    private String country;
    private String phone;
    private String email;
    private String race;
    private String ethnicity;
    private String gender;
    private boolean veteran;
    private boolean disability;

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVeteran() {
        return veteran;
    }

    public void setVeteran(boolean veteran) {
        this.veteran = veteran;
    }

    public boolean isDisability() {
        return disability;
    }

    public void setDisability(boolean disability) {
        this.disability = disability;
    }

    public void save(){
        File md = new File(Start.DIR + "\\.store\\EMPS\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".emps")) {
                    Ledger fl = Json.load(file.getPath(), Ledger.class);
                    if (fl.getId().equals(id)) {
                        Json.save(file.getPath(), this);
                    }
                }
            }
        }
    }
}