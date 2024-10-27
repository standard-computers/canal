package org.Canal.Models.SupplyChainUnits;

import java.util.HashMap;
import java.util.Map;

public class Flex {

    protected Map<String, String> properties;

    public Flex(Map<String, String> properties) {
        this.properties = properties;
    }

    public Flex() {
        properties = new HashMap<>();
        properties.put("id", "");
        properties.put("name", "");
        properties.put("cost_center", "");
        properties.put("height", "");
        properties.put("width", "");
        properties.put("length", "");
    }

    public Flex(String id, String cc) {
        properties = new HashMap<>();
        properties.put("id", id);
        properties.put("name", "");
        properties.put("cost_center", cc);
    }

    public Flex(String id) {
        properties = new HashMap<>();
        properties.put("id", id);
        properties.put("name", "");
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