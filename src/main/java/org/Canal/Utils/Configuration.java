package org.Canal.Utils;

public class Configuration {

    private String endpoint = "127.0.0.1";
    private String instance_name = "Canal";
    private String product_key = "";
    private String theme = "/com/formdev/flatlaf/intellijthemes/themes/SolarizedDark.theme.json";
    private String assignedUser; //ID of User this instance of Canal is assigned to
    private boolean showCanalCodes = false;
    private boolean showButtonLabels = true;

    public String getEndpoint() {
        return endpoint;
    }

    public String getInstance_name() {
        return instance_name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean showCanalCodes() {
        return showCanalCodes;
    }

    public void setShowCanalCodes(boolean showCanalCodes) {
        this.showCanalCodes = showCanalCodes;
    }

    public boolean isShowButtonLabels() {
        return showButtonLabels;
    }

    public void setShowButtonLabels(boolean showButtonLabels) {
        this.showButtonLabels = showButtonLabels;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Override
    public String toString() {
        return instance_name;
    }
}