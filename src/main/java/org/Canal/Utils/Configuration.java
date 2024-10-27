package org.Canal.Utils;

import org.Canal.Models.SupplyChainUnits.Transaction;
import java.util.ArrayList;

public class Configuration {

    private String endpoint = "127.0.0.1", instance_name = "Canal", product_key = "";
    private String theme = "/com/formdev/flatlaf/intellijthemes/themes/SolarizedDark.theme.json";
    private String defaultModule = "/";
    private int port = 4567;
    private boolean encrypted = false;
    private ArrayList<Transaction> transactions;

    public Configuration(String endpoint, String instanceName) {
        this.endpoint = endpoint;
        this.instance_name = instanceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getInstance_name() {
        return instance_name;
    }

    public void setInstance_name(String instance_name) {
        this.instance_name = instance_name;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDefaultModule() {
        return defaultModule;
    }

    public void setDefaultModule(String defaultModule) {
        this.defaultModule = defaultModule;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return instance_name;
    }
}