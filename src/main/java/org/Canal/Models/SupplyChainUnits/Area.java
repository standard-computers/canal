package org.Canal.Models.SupplyChainUnits;

import java.util.HashMap;
import java.util.Map;

public class Area {

    private String id, location, name;
    private double width, length, height, area, volume;

    protected Map<String, String> properties;

    public Area(Map<String, String> properties) {
        this.properties = properties;
    }

    public Area(String id, String cc) {
        properties = new HashMap<>();
        properties.put("id", id);
        properties.put("name", "");
        properties.put("cost_center", cc);
        properties.put("width", "");
        properties.put("length", "");
        properties.put("height", "");
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> defaultProperties) {
        this.properties = defaultProperties;
    }

    public String getValue(String key) {
        return properties.get(key);
    }

    public void updateProperties(Map<String, String> updatedProperties) {
        properties.putAll(updatedProperties);
    }

    public String getId(){
        return properties.get("id");
    }

    @Override
    public String toString() {
        return getId();
    }
}