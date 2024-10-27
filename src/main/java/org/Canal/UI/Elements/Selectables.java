package org.Canal.UI.Elements;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Utils.Engine;
import java.util.HashMap;

public class Selectables {

    public static Selectable allLocations(){
        HashMap<String, String> availableLocations = new HashMap<>();
        for(Location cs : Engine.getCostCenters()){
            availableLocations.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Location dcs : Engine.getDistributionCenters()){
            availableLocations.put(dcs.getId() + " – " + dcs.getName(), dcs.getId());
        }
        for(Location whs : Engine.getWarehouses()){
            availableLocations.put(whs.getId() + " – " + whs.getName(), whs.getId());
        }
        return new Selectable(availableLocations);
    }

    public static Selectable statusTypes(){
        HashMap<String, String> statusTypes = new HashMap<>();
        statusTypes.put("APPROVED", "APPROVED");
        statusTypes.put("ARCHIVED", "ARCHIVED");
        statusTypes.put("BLOCKED", "BLOCKED");
        statusTypes.put("COMPLETED", "COMPLETED");
        statusTypes.put("DELIVERED", "DELIVERED");
        statusTypes.put("DRAFT", "DRAFT");
        statusTypes.put("DELINQUENT", "DELINQUENT");
        statusTypes.put("ERRORED", "ERRORED");
        statusTypes.put("IN TRANSIT", "IN_TRANSIT");
        statusTypes.put("NEW", "NEW");
        statusTypes.put("OPEN", "OPEN");
        statusTypes.put("PENDING", "PENDING");
        statusTypes.put("REMOVED", "REMOVED");
        statusTypes.put("SUSPENDED", "SUSPENDED");
        return new Selectable(statusTypes);
    }
}