package org.Canal.Utils;

import java.util.HashMap;

public class Configuration {

    private String endpoint = "127.0.0.1";
    private String instance_name = "Canal";
    private String product_key = "";
    private String theme = "/com/formdev/flatlaf/intellijthemes/themes/SolarizedDark.theme.json";
    private String defaultModule = "/";
    private String assignedUser; //ID of User this instance of Canal is assigned to
    private int port = 4567;
    private boolean encrypted = false;
    private boolean showCanalCodes = false;
    private HashMap<String, String> variables = new HashMap<>();

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

    public void setDefaultModule(String defaultModule) {
        this.defaultModule = defaultModule;
    }

    public boolean showCanalCodes() {
        return showCanalCodes;
    }

    public void setShowCanalCodes(boolean showCanalCodes) {
        this.showCanalCodes = showCanalCodes;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, String> variables) {
        this.variables = variables;
    }

    public void addVariable(String key, String value){
        variables.put(key, value);
    }

    public void removeVariable(String key){
        variables.remove(key);
    }

    public void getVariable(String key){
        variables.get(key);
    }

    public void adjustVariable(String key, String value){
        variables.replace(key, value);
    }

    @Override
    public String toString() {
        return instance_name;
    }
}