package org.Canal.Utils;

import java.util.HashMap;

public class Codex {

    HashMap<String, HashMap<String, Object>> variables = new HashMap<>();

    public Codex() {
        variables.put("/", new HashMap<>() {{
            put("minumim_build", 1); //What Min build version should clients have?
            put("currency_symbol", "$");
            put("default_currency", "USD");
            put("exit_auto_logout", false);
            put("hard_enforce_flows", false);
            put("default_tax_rate", 0.085);
            put("auto_override_rcv_loc", false);
            put("use_deliveries", true);
            put("perform_ff_stock_check", false);
            put("vendor_management", false); //Can make users for vendors (Enable XFunctionality)
            put("vendor_managed_inventory", false); //Vendor can manage their inventory & POs with their login
            put("vendor_managed_ledger", false); //Maintain ledger for vendor
        }});
        variables.put("ORGS", new HashMap<>() {{
            put("name", "Organizations");
            put("prefix", "");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("icon", "organizations");
            put("allow_archival", true);
            put("allow_deletion", true);
            put("auto_putaway", false);
            put("autogen_gr_on_receive", true);
            put("automake_enabled", true);
            put("autowave_enabled", false);
            put("calculate_bin_size", false);
            put("check_area_size", false);
            put("check_bin_size", false);
            put("import_enabled", false);
            put("export_enabled", false);
            put("list_refresh_rate", 10);
            put("require_palletization", false);
            put("repeat_create", false);
            put("single_order_pur_req", false);
            put("hu_length", 10);
            put("start_maximized", true);
            put("auto_open_new", false);
        }});
        variables.put("DCSS", new HashMap<>() {{
            put("name", "Distribution Centers");
            put("icon", "distributioncenters");
            put("prefix", "DC");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
            put("auto_open_new", false);
        }});
        variables.put("CCS", new HashMap<>() {{
            put("name", "Cost Centers");
            put("icon", "costcenters");
            put("prefix", "");
            put("leading_number", 1);
            put("leading_zeros", 4);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
            put("auto_open_new", false);
        }});
        variables.put("OFFS", new HashMap<>() {{
            put("name", "Offices");
            put("icon", "costcenters");
            put("prefix", "LOC");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
            put("auto_open_new", false);
        }});
        variables.put("VEND", new HashMap<>() {{
            put("name", "Vendors");
            put("icon", "vendors");
            put("prefix", "V");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
            put("auto_open_new", false);
        }});
        variables.put("WHS", new HashMap<>() {{
            put("name", "Warehouses");
            put("icon", "warehouses");
            put("prefix", "WH");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
            put("auto_open_new", false);
        }});
        variables.put("CSTS", new HashMap<>() {{
            put("name", "Customers");
            put("icon", "customers");
            put("prefix", "C");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
        }});
        variables.put("ACCS", new HashMap<>() {{
            put("name", "Accounts");
            put("icon", "accounts");
            put("prefix", "#");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("require_agreements", false);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
        }});
        variables.put("AREAS", new HashMap<>() {{
            put("name", "Areas");
            put("icon", "areas");
            put("prefix", "A");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
        }});
        variables.put("BNS", new HashMap<>() {{
            put("name", "Bins");
            put("icon", "bins");
            put("prefix", "BN");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("find_limit", 200);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
        }});
        variables.put("DPTS", new HashMap<>() {{
            put("name", "Bins");
            put("icon", "bins");
            put("prefix", "BN");
            put("leading_number", 1);
            put("leading_zeros", 1);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("auto_open_new", false);
            put("start_maximized", true);
        }});
        variables.put("HR/POS", new HashMap<>() {{
            put("name", "Positions");
            put("icon", "positions");
            put("prefix", "POS-RQ");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("item_created_alert", false);
            put("dispose_on_save", true);
            put("auto_open_new", false);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("EMPS", new HashMap<>() {{
            put("name", "People");
            put("icon", "employees");
            put("prefix", "E");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("allow_modification", true);
            put("import_enabled", false);
            put("export_enabled", false);
            put("enable_email_option", true);
            put("dispose_on_save", true);
            put("auto_open_new", false);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("start_maximized", true);
        }});
        variables.put("USRS", new HashMap<>() {{
            put("name", "Users");
            put("icon", "users");
            put("prefix", "U");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("allow_modification", true);
            put("import_enabled", false);
            put("export_enabled", false);
            put("automake_enabled", true); //AutoMake for users creates a user for each employee that doesn't have one
            put("allow_batch_create", true);
            put("start_maximized", false);
        }});
        variables.put("LGS", new HashMap<>() {{
            put("name", "Ledgers");
            put("icon", "ledgers");
            put("prefix", "GL");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("RTS", new HashMap<>() {{
            put("name", "Rates");
            put("icon", "ledgers");
            put("prefix", "");
            put("leading_number", 1);
            put("leading_zeros", 3);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("auto_open_new", false);
            put("dispose_on_save", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("ORDS/PO", new HashMap<>() {{
            put("name", "Purchase Orders");
            put("icon", "orders");
            put("prefix", "PO");
            put("leading_number", 1);
            put("leading_zeros", 6);
            put("commit_to_ledger", true);
            put("use_deliveries", true); //Creates associated delivery
            put("allow_deletion", true); //Allow deletion of POs
            put("allow_archival", true); //Allow archival of POs
            put("import_enabled", false); //Allows POs to be created by AutoMake
            put("export_enabled", true);
            put("allow_batch_create", true); //Allows POs to be created by job
            put("require_pr", false); //Require Purchase Req on for PO
            put("require_bill_ship", false); //Bill To and Ship To must be the same
            put("vendor_match", true);
            put("start_maximized", true);
        }});
        variables.put("ORDS/SO", new HashMap<>() {{
            put("name", "Sales Orders");
            put("icon", "orders");
            put("prefix", "SO");
            put("leading_number", 1);
            put("leading_zeros", 6);
            put("commit_to_ledger", true);
            put("use_deliveries", true);
            put("auto_create_po", true);
            put("create_buyer_inbound", true);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", false);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("ORDS/PR", new HashMap<>() {{
            put("name", "Purchase Requisitions");
            put("icon", "orders");
            put("prefix", "PR");
            put("leading_number", 1);
            put("leading_zeros", 6);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", false);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("ORDS/RCV", new HashMap<>() {{
            put("block_locations", true); //If not ship-to, can't receive
            put("create_goods_receipt", true); //Creates Goods Receipt on receive
            put("allow_loc_block_override", true); //Presents override option for 'block_locations
            put("always_explode_package", false);
            put("allow_explode_package", true);
            put("require_skus", true); //If received product is sku'd requires SKU be provided
        }});
        variables.put("GR", new HashMap<>() {{
            put("name", "Goods Receipts");
            put("prefix", "GR");
            put("leading_number", 1);
            put("leading_zeros", 5);
//            put("commit_to_ledger", false); //TODO Temp until remove
            put("commit_to_ledger", true);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("GI", new HashMap<>() {{
            put("name", "Goods Issues");
            put("prefix", "GI");
            put("leading_number", 1);
            put("leading_zeros", 5);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("CATS", new HashMap<>() {{
            put("name", "Catalogs");
            put("prefix", "CT");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("ITS", new HashMap<>() {{
            put("name", "Items");
            put("prefix", "X0");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("allow_modification", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("allow_batch_create", true);
            put("item_created_alert", false);
            put("auto_open_new", false);
            put("dispose_on_save", true);
            put("default_width_uom", "IN");
            put("default_length_uom", "IN");
            put("default_height_uom", "IN");
            put("default_weight_uom", "OZ");
            put("start_maximized", true);
        }});
        variables.put("BOMS", new HashMap<>() {{
            put("name", "Bill of Materials");
            put("prefix", "B");
            put("leading_number", 1);
            put("leading_zeros", 3);
            put("allow_modification", true);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("start_maximized", true);
        }});
        variables.put("MOS", new HashMap<>() {{
            put("name", "Manufacturing Orders");
            put("prefix", "MO");
            put("leading_number", 1);
            put("leading_zeros", 4);
            put("allow_modification", true);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("start_maximized", true);
        }});
        variables.put("MTS", new HashMap<>() {{
            put("name", "Materials");
            put("prefix", "M0");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_modification", true);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("start_maximized", true);
        }});
        variables.put("CMPS", new HashMap<>() {{
            put("name", "Components");
            put("prefix", "CP0");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("TRANS/CRRS", new HashMap<>() {{
            put("name", "Transportation Carriers");
            put("icon", "carriers");
            put("prefix", "CRR");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("import_enabled", true);
            put("export_enabled", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("TRANS/ODO", new HashMap<>() {{
            put("name", "Outbound Deliveries");
            put("prefix", "OBD");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("TRANS/IDO", new HashMap<>() {{
            put("name", "Inbound Deliveries");
            put("prefix", "IND");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("TRANS/TRCKS", new HashMap<>() {{
            put("name", "Trucks");
            put("prefix", "TR");
            put("leading_number", 1);
            put("leading_zeros", 2);
            put("allow_archival", true);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
            put("start_maximized", true);
        }});
        variables.put("STL", new HashMap<>() {{
            put("name", "Stock Lines");
            put("prefix", "ST");
            put("length", 6);
            put("allow_archival", false);
            put("allow_deletion", true);
            put("automake_enabled", true);
            put("allow_batch_create", true);
        }});
    }

    public HashMap<String, HashMap<String, Object>> getVariables() {
        return variables;
    }

    public Object getValue(String outerKey, String innerKey) {
        if(outerKey.startsWith("/")){
            outerKey = outerKey.toUpperCase().replaceFirst("/", "");
        }
        HashMap<String, Object> innerMap = variables.get(outerKey);
        if (innerMap != null) {
            return innerMap.get(innerKey);
        }
        return null; // Return null if the outer or inner key does not exist
    }
}