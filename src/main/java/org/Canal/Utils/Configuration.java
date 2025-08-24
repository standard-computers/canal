package org.Canal.Utils;

public class Configuration {

    private String endpoint = "127.0.0.1"; //Server endpoint connection string
    private String mongodb = "mongodb://localhost:27017"; //MongoDB Server connection string
    private String theme = "/com/formdev/flatlaf/intellijthemes/themes/SolarizedDark.theme.json";
    private String background;
    private String assignedUser; //ID of User this instance of Canal is assigned to
    private int fontSize = 14;
    private boolean saveLockeState = false;
    private boolean showCanalCodes = false;
    private boolean showButtonLabels = true;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMongodb() {
        return mongodb;
    }

    public void setMongodb(String mongodb) {
        this.mongodb = mongodb;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public boolean saveLockeState() {
        return saveLockeState;
    }

    public void setSaveLockeState(boolean saveLockeState) {
        this.saveLockeState = saveLockeState;
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

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String toString() {
        return endpoint;
    }
}