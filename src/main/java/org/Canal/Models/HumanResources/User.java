package org.Canal.Models.HumanResources;

import java.util.ArrayList;

public class User {

    private String id, employee;
    private ArrayList<String> accesses = new ArrayList<>();
    private String fontSize, theme;

    public User(String id, String employee) {
        this.id = id;
        this.employee = employee;
    }

    public String getEmployee() {
        return employee;
    }

    public String getId() {
        return id;
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
}