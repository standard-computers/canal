package org.Canal.Models.HumanResources;

import org.Canal.Utils.Crypter;

import javax.swing.*;
import java.util.ArrayList;

public class User {

    private String id; //User ID for system
    private String employee; //Employee ID User is tied to
    private ArrayList<String> accesses = new ArrayList<>(); //List of transaction codes User can use
    private String fontSize;
    private String theme;
    private String hpv;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public ArrayList<String> getAccesses() {
        return accesses;
    }

    public void setAccesses(ArrayList<String> accesses) {
        this.accesses = accesses;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getHpv() {
        return hpv;
    }

    public void setHpv(String hpv) {
        this.hpv = hpv;
    }

    public boolean hasPassword(String attempt){
        try {
            if(Crypter.md5(attempt).equals(hpv)){
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
        return false;
    }
}