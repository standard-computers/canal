package org.Canal.Models.SupplyChainUnits;

import java.util.HashMap;

/**
 * For inventory and storage of items.
 */
public class Door extends Flex {

    private String name, location, area;
    private int[] coordinates;
    private int min, max; //Quantity of items it may contain

    public Door(){
        properties = new HashMap<>();
        properties.put("id", "");
        properties.put("name", "");
        properties.put("area", "");
        properties.put("height", "");
        properties.put("width", "");
        properties.put("length", "");
        properties.put("weight", "");
    }
}