package org.Canal.UI.Elements;

import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Warehouse;
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
        for(Warehouse whs : Engine.getWarehouses()){
            availableLocations.put(whs.getId() + " – " + whs.getName(), whs.getId());
        }
        return new Selectable(availableLocations);
    }

    public static Selectable allEmployees(){
        HashMap<String, String> availableEmps = new HashMap<>();
        for(Employee emp : Engine.getEmployees()){
            availableEmps.put(emp.getId() + " – " + emp.getName(), emp.getId());
        }
        return new Selectable(availableEmps);
    }

    public static Selectable allOrgs(){
        HashMap<String, String> availableOrgs = new HashMap<>();
        for(Organization org : Engine.getOrganizations()){
            availableOrgs.put(org.getId() + " – " + org.getName(), org.getId());
        }
        return new Selectable(availableOrgs);
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

    public static Selectable countries() {
        HashMap<String, String> statusTypes = new HashMap<>();
        statusTypes.put("United States", "US");
        statusTypes.put("United Kingdom", "GB");
        statusTypes.put("Canada", "CA");
        statusTypes.put("Australia", "AU");
        statusTypes.put("Japan", "JP");
        statusTypes.put("Germany", "DE");
        statusTypes.put("France", "FR");
        statusTypes.put("Italy", "IT");
        statusTypes.put("Spain", "ES");
        statusTypes.put("Netherlands", "NL");
        statusTypes.put("Switzerland", "CH");
        statusTypes.put("Sweden", "SE");
        statusTypes.put("Norway", "NO");
        statusTypes.put("Denmark", "DK");
        statusTypes.put("Finland", "FI");
        statusTypes.put("Ireland", "IE");
        statusTypes.put("Belgium", "BE");
        statusTypes.put("Austria", "AT");
        statusTypes.put("New Zealand", "NZ");
        statusTypes.put("South Korea", "KR");
        statusTypes.put("Singapore", "SG");
        statusTypes.put("Luxembourg", "LU");
        return new Selectable(statusTypes);
    }
}