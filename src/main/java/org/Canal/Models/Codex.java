package org.Canal.Models;

import java.util.HashMap;

public class Codex {

    HashMap<String, HashMap<String, Object>> variables = new HashMap<>();

    public Codex() {
        variables.put("/", new HashMap<String, Object>() {{
            put("currency_symbol", "$");
            put("default_currency", "USD");
            put("exit_auto_logout", false);
            put("hard_enforce_flows", false);
            put("default_tax_rate", 0.085);
            put("auto_override_rcv_loc", false);
        }});
        variables.put("ORGS", new HashMap<String, Object>() {{
            put("name", "Organizations");
            put("prefix", "");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("auto_putaway", false);
            put("autogen_gr_on_receive", true);
            put("automake_enabled", true);
            put("autowave_enabled", false);
            put("calculate_bin_size", false);
            put("check_area_size", false);
            put("check_bin_size", false);
            put("list_refresh_rate", 10);
            put("require_palletization", false);
            put("repeat_create", false);
            put("single_order_pur_req", false);
            put("hu_length", 10);
        }});
        variables.put("DCSS", new HashMap<String, Object>() {{
            put("name", "Distribution Centers");
            put("prefix", "DC");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
        variables.put("CCS", new HashMap<String, Object>() {{
            put("name", "Cost Centers");
            put("prefix", "CC");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
        variables.put("VEND", new HashMap<String, Object>() {{
            put("name", "Vendors");
            put("prefix", "V");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
        variables.put("WHS", new HashMap<String, Object>() {{
            put("name", "Warehouses");
            put("prefix", "WH");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
        variables.put("CSTS", new HashMap<String, Object>() {{
            put("name", "Customers");
            put("prefix", "C");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
        variables.put("AREAS", new HashMap<String, Object>() {{
            put("name", "Areas");
            put("prefix", "A");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
        variables.put("BNS", new HashMap<String, Object>() {{
            put("name", "Bins");
            put("prefix", "BN");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
        }});
    }

    public Object getValue(String outerKey, String innerKey) {
        outerKey = outerKey.toUpperCase().replace("/", "");
        HashMap<String, Object> innerMap = variables.get(outerKey);
        if (innerMap != null) {
            return innerMap.get(innerKey);
        }
        return null; // Return null if the outer or inner key does not exist
    }
}
