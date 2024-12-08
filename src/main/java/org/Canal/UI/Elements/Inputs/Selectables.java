package org.Canal.UI.Elements.Inputs;

import com.formdev.flatlaf.IntelliJTheme;
import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Selectables {

    public static Selectable allLocations(){
        HashMap<String, String> availableLocations = new HashMap<>();
        for(Location cs : Engine.getCostCenters()){
            availableLocations.put(cs.getId(), cs.getId());
        }
        for(Location dcs : Engine.getDistributionCenters()){
            availableLocations.put(dcs.getId(), dcs.getId());
        }
        for(Warehouse whs : Engine.getWarehouses()){
            availableLocations.put(whs.getId(), whs.getId());
        }
        for(Vendor vndr : Engine.getVendors()){
            availableLocations.put(vndr.getId(), vndr.getId());
        }
        return new Selectable(availableLocations);
    }

    public static Selectable employees(){
        HashMap<String, String> employees = new HashMap<>();
        for(Employee emp : Engine.getEmployees()){
            employees.put(emp.getId(), emp.getId());
        }
        Selectable s =  new Selectable(employees);
        s.editable();
        return s;
    }

    public static Selectable organizations(){
        HashMap<String, String> organizations = new HashMap<>();
        for(Organization org : Engine.getOrganizations()){
            organizations.put(org.getId(), org.getId());
        }
        return new Selectable(organizations);
    }

    public static Selectable areas(){
        HashMap<String, String> areas = new HashMap<>();
        for(Area a : Engine.getAreas()){
            areas.put(a.getId(), a.getId());
        }
        return new Selectable(areas);
    }

    public static Selectable vendors(){
        HashMap<String, String> vendors = new HashMap<>();
        for(Vendor v : Engine.getVendors()){
            vendors.put(v.getId(), v.getId());
        }
        Selectable s =  new Selectable(vendors);
        s.editable();
        return s;
    }

    public static Selectable customers(){
        HashMap<String, String> customers = new HashMap<>();
        for(Location v : Engine.getCustomers()){
            customers.put(v.getId(), v.getId());
        }
        Selectable s =  new Selectable(customers);
        s.editable();
        return s;
    }

    public static Selectable departments(){
        HashMap<String, String> departments = new HashMap<>();
        for(Department org : Engine.getOrganization().getDepartments()){
            departments.put(org.getId(), org.getId());
        }
        Selectable s =  new Selectable(departments);
        s.editable();
        return s;
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

    public static Selectable periods(){
        HashMap<String, String> statusTypes = new HashMap<>();
        statusTypes.put("Q1", "Q1");
        statusTypes.put("Q2", "Q2");
        statusTypes.put("Q3", "Q3");
        statusTypes.put("Q4", "Q4");
        return new Selectable(statusTypes);
    }

    public static Selectable ethnicities(){
        HashMap<String, String> statusTypes = new HashMap<>();
        statusTypes.put("CAUCASION", "CAUCASION");
        statusTypes.put("AFRICAN", "AFRICAN");
        statusTypes.put("NATIVE AMERICAN", "NATIVE AMERICAN");
        statusTypes.put("ASIAN", "ASIAN");
        return new Selectable(statusTypes);
    }

    public static Selectable genders(){
        HashMap<String, String> statusTypes = new HashMap<>();
        statusTypes.put("MALE", "MALE");
        statusTypes.put("FEMALE", "FEMALE");
        statusTypes.put("UNIDENTIFIED", "UNIDENTIFIED");
        return new Selectable(statusTypes);
    }

    public static Selectable uoms(String preset){
        HashMap<String, String> statusTypes = new HashMap<>();
        statusTypes.put("MM", "MM");
        statusTypes.put("CM", "CM");
        statusTypes.put("CM2", "CM2");
        statusTypes.put("CM3", "CM3");
        statusTypes.put("IN", "IN");
        statusTypes.put("IN2", "IN2");
        statusTypes.put("IN3", "IN3");
        statusTypes.put("FT", "FT");
        statusTypes.put("FT2", "FT2");
        statusTypes.put("FT3", "FT3");
        statusTypes.put("SQ FT", "SQ FT");
        statusTypes.put("M", "M");
        statusTypes.put("M2", "M2");
        statusTypes.put("M3", "M3");
        statusTypes.put("YD", "YD");
        statusTypes.put("MI", "MI");
        statusTypes.put("NM", "NM");
        statusTypes.put("KM", "KM");
        statusTypes.put("OZ", "OZ");
        statusTypes.put("LBS", "LBS");
        statusTypes.put("KGS", "KGS");
        statusTypes.put("TNS", "TNS");
        Selectable uomField = new Selectable(statusTypes);
        uomField.setSelectedValue(preset);
        uomField.editable();
        return uomField;
    }

    public static Selectable uoms(){
        return uoms("FT");
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
        Selectable s = new Selectable(statusTypes);
        s.setSelectedValue("US");
        return s;
    }
    public static Selectable themes() {
        HashMap<String, String> themeMap = new HashMap<>();
        themeMap.put("Arc", "/com/formdev/flatlaf/intellijthemes/themes/arc-theme.theme.json");
        themeMap.put("Arc Dark", "/com/formdev/flatlaf/intellijthemes/themes/arc_theme_dark.theme.json");
        themeMap.put("Arc Orange", "/com/formdev/flatlaf/intellijthemes/themes/arc-theme-orange.theme.json");
        themeMap.put("Arc Dark Orange", "/com/formdev/flatlaf/intellijthemes/themes/arc_theme_dark_orange.theme.json");
        themeMap.put("Carbon", "/com/formdev/flatlaf/intellijthemes/themes/Carbon.theme.json");
        themeMap.put("Cobalt 2", "/com/formdev/flatlaf/intellijthemes/themes/Cobalt_2.theme.json");
        themeMap.put("Cyan", "/com/formdev/flatlaf/intellijthemes/themes/Cyan.theme.json");
        themeMap.put("Dark Flat", "/com/formdev/flatlaf/intellijthemes/themes/DarkFlatTheme.theme.json");
        themeMap.put("Dark Purple", "/com/formdev/flatlaf/intellijthemes/themes/DarkPurple.theme.json");
        themeMap.put("Dracula", "/com/formdev/flatlaf/intellijthemes/themes/Dracula.theme.json");
        themeMap.put("Gradianto Dark Fuchsia", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_dark_fuchsia.theme.json");
        themeMap.put("Gradianto Deep Ocean", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_deep_ocean.theme.json");
        themeMap.put("Gradianto Midnight Blue", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_midnight_blue.theme.json");
        themeMap.put("Gradianto Nature Green", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_Nature_Green.theme.json");
        themeMap.put("Gray", "/com/formdev/flatlaf/intellijthemes/themes/Gray.theme.json");
        themeMap.put("Gruvbox Dark Hard", "/com/formdev/flatlaf/intellijthemes/themes/gruvbox_dark_hard.theme.json");
        themeMap.put("Gruvbox Dark Medium", "/com/formdev/flatlaf/intellijthemes/themes/gruvbox_dark_medium.theme.json");
        themeMap.put("Gruvbox Dark Soft", "/com/formdev/flatlaf/intellijthemes/themes/gruvbox_dark_soft.theme.json");
        themeMap.put("Solarized Light", "/com/formdev/flatlaf/intellijthemes/themes/SolarizedLight.theme.json");
        themeMap.put("Solarized Dark", "/com/formdev/flatlaf/intellijthemes/themes/SolarizedDark.theme.json");
        themeMap.put("Flat Monocai", "/com/formdev/flatlaf/intellijthemes/themes/Monocai.theme.json");
        themeMap.put("Flat Monocai Pro", "/com/formdev/flatlaf/intellijthemes/themes/Monokai_Pro.default.theme.json");
        themeMap.put("Nord", "/com/formdev/flatlaf/intellijthemes/themes/nord.theme.json");
        themeMap.put("One Dark", "/com/formdev/flatlaf/intellijthemes/themes/one_dark.theme.json");
        themeMap.put("Vuesion", "/com/formdev/flatlaf/intellijthemes/themes/vuesion_theme.theme.json");
        themeMap.put("Xcode Dark", "/com/formdev/flatlaf/intellijthemes/themes/Xcode-Dark.theme.json");
        themeMap.put("Hiberbee Dark", "/com/formdev/flatlaf/intellijthemes/themes/HiberbeeDark.theme.json");
        themeMap.put("Spacegray", "/com/formdev/flatlaf/intellijthemes/themes/Spacegray.theme.json");
        themeMap.put("High Contrast", "/com/formdev/flatlaf/intellijthemes/themes/HighContrast.theme.json");
        themeMap.put("Light Flat", "/com/formdev/flatlaf/intellijthemes/themes/LightFlatTheme.theme.json");
        themeMap.put("Material Theme", "/com/formdev/flatlaf/intellijthemes/themes/MaterialTheme.theme.json");
        themeMap.put("~Material Arc Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Arc Dark.theme.json");
        themeMap.put("~Material Atom One Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Atom One Dark.theme.json");
        themeMap.put("~Material Atom One Light", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Atom One Light.theme.json");
        themeMap.put("~Material Dracula", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Dracula.json");
        themeMap.put("~Material GitHub", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/GitHub.theme.json");
        themeMap.put("~Material GitHub Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/GitHub Dark.theme.json");
        themeMap.put("~Material Light Owl", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Light Owl.theme.json");
        themeMap.put("~Material Material Darker", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Darker.theme.json");
        themeMap.put("~Material Material Deep Ocean", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Deep Ocean.theme.json");
        themeMap.put("~Material Material Lighter", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Lighter.theme.json");
        themeMap.put("~Material Material Oceanic", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Oceanic.theme.json");
        themeMap.put("~Material Material Palenight", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Palenight.theme.json");
        themeMap.put("~Material Monokai Pro", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Monokai Pro.theme.json");
        themeMap.put("~Material Night Owl", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Night Owl.theme.json");
        themeMap.put("~Material Solarized Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Solarized Dark.theme.json");
        themeMap.put("~Material Solarized Light", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Solarized Light.theme.json");
        Selectable s = new Selectable(themeMap);
        s.addActionListener(_ -> {
            String selectedTheme = (String) s.getSelected();
            if (selectedTheme != null) {
                String themePath = s.getSelectedValue();
                try {
                    IntelliJTheme.setup(Selectable.class.getResourceAsStream(themePath));
                    for (Window window : Window.getWindows()) {
                        SwingUtilities.updateComponentTreeUI(window);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return s;
    }
}