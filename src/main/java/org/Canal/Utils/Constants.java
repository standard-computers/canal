package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Constants {

    public final static Color[] colors = new Color[]{
        new Color(251, 88, 88),
        new Color(255, 134, 92),
        new Color(255, 173, 92),
        new Color(251, 251, 85),
        new Color(199, 253, 88),
        new Color(170, 253, 88),
        new Color(88, 251, 169),
        new Color(87, 196, 251),
        new Color(93, 135, 251),
        new Color(93, 93, 255),
        new Color(171, 90, 253),
        new Color(251, 90, 251)
    };
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateId(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }

    public static ArrayList<String> getAllTransactions() {
        ArrayList<String> transactions = new ArrayList<>();
        if (allModules().getTransaction() != null) {
            allModules();
            transactions.add(allModules().getTransaction());
        }
        if (allModules().getChildren() != null) {
            for (Locke child : allModules().getChildren()) {
                transactions.addAll(getTransactions(child));
            }
        }
        return removeDuplicates(transactions);
    }

    public static ArrayList<String> getTransactions(Locke canal) {
        ArrayList<String> transactions = new ArrayList<>();
        if (canal.getTransaction() != null) {
            transactions.add(canal.getTransaction());
        }
        if (canal.getChildren() != null) {
            for (Locke child : canal.getChildren()) {
                transactions.addAll(getTransactions(child));
            }
        }
        return removeDuplicates(transactions);
    }

    private static ArrayList<String> removeDuplicates(ArrayList<String> transactions) {
        ArrayList<String> uniqueTransactions = new ArrayList<>();
        for (String transaction : transactions) {
            if (!uniqueTransactions.contains(transaction)) {
                uniqueTransactions.add(transaction);
            }
        }
        return uniqueTransactions;
    }

    public static Locke allModules() {
        return new Locke("Canal – All Actions", getIcon("controller"), "/ORGS", new Locke[]{
                new Locke("Quick Accesses", getIcon("controllers"), "/", new Locke[]{
                        new Locke("My Profile", getIcon("locke"), "/ME", null),
                        new Locke("Finance", getIcon("finance"), "/FI", null),
                        new Locke("Inventory", getIcon("locke"), "/INV", null),
                        new Locke("Human Resources", getIcon("humanresources"), "/HR", null),
                        new Locke("Time Clock", getIcon("timeclock"), "/TM_CLCK", null),
                        new Locke("Clear Desk", getIcon("locke"), "/CLEAR_DSK", null),
                        new Locke("Close Desk", getIcon("locke"), "/CLOSE_DSK", null),
                        new Locke("Canal Settings", getIcon("settings"), "/CNL", null),
                        new Locke("Data Center", getIcon("datacenter"), "/DATA_CNTR", null),
                        new Locke("Restart Canal", getIcon("locke"), "/RSTRT", null),
                        new Locke("Leave Canal", getIcon("locke"), "/EXIT", null),
                }),
                new Locke("Organizations", getIcon("organizations"), "/ORGS", new Locke[]{
                        new Locke("Find Organization", getIcon("find"), "/ORGS/F", null),
                        new Locke("Create an Organization", getIcon("create"), "/ORGS/NEW", null),
                        new Locke("Modify an Organization", getIcon("modify"), "/ORGS/MOD", null),
                        new Locke("Archive an Organization", getIcon("archive"), "/ORGS/ARCHV", null),
                        new Locke("Remove an Organization", getIcon("delete"), "/ORGS/DEL", null),
                        new Locke("Departments", getIcon("departments"), "/DPTS",  new Locke[]{
                                new Locke("Find Department", getIcon("find"), "/DPTS/F", null),
                                new Locke("Create a Department", getIcon("create"), "/DPTS/NEW", null),
                                new Locke("Modify Department", getIcon("locke"), "/DPTS/MOD", null),
                                new Locke("Archive Department", getIcon("archive"), "/DPTS/ARCHV", null),
                                new Locke("Remove Department", getIcon("delete"), "/DPTS/DEL", null),
                        }),
                        new Locke("Positions", getIcon("positions"), "/HR/POS",  new Locke[]{
                                new Locke("Find Position", getIcon("find"), "/HR/POS/F", null),
                                new Locke("Create a Position", getIcon("create"), "/HR/POS/NEW", null),
                                new Locke("Post Position", getIcon("locke"), "/HR/POS/POST", null),
                                new Locke("Modify Position", getIcon("modify"), "/HR/POS/MOD", null),
                                new Locke("Archive Position", getIcon("archive"), "/HR/POS/ARCHV", null),
                                new Locke("Remove Position", getIcon("delete"), "/HR/POS/DEL", null),
                        }),
                        new Locke("Teams", getIcon("teams"), "/TMS",  new Locke[]{
                                new Locke("Find Team", getIcon("find"), "/TMS/F", null),
                                new Locke("Create a Team", getIcon("create"), "/TMS/NEW", null),
                                new Locke("Modify Team", getIcon("modify"), "/TMS/MOD", null),
                                new Locke("Archive Team", getIcon("archive"), "/TMS/ARCHV", null),
                                new Locke("Remove Team", getIcon("delete"), "/TMS/DEL", null),
                        }),
                }),
                new Locke("Cost Centers", getIcon("costcenters"), "/CCS", new Locke[]{
                        new Locke("Find Cost Center", getIcon("find"), "/CCS/F", null),
                        new Locke("Create a Cost Center", getIcon("create"), "/CCS/NEW", null),
                        new Locke("Modify Cost Center", getIcon("modify"), "/CCS/MOD", null),
                        new Locke("Archive Cost Center", getIcon("archive"), "/CCS/ARCHV", null),
                        new Locke("Remove Cost Center", getIcon("delete"), "/CCS/DEL", null),
                }),
                new Locke("Distribution Centers", getIcon("distributioncenters"), "/DCSS", new Locke[]{
                        new Locke("Find Distribution Center", getIcon("find"), "/DCSS/F", null),
                        new Locke("Create Dist. Center", getIcon("create"), "/DCSS/NEW", null),
                        new Locke("Modify Dist. Center", getIcon("modify"), "/DCSS/MOD", null),
                        new Locke("Archive Dist. Center", getIcon("archive"), "/DCSS/ARCHV", null),
                        new Locke("Remove Dist. Center", getIcon("delete"), "/DCSS/DEL", null),
                }),
                new Locke("Warehouses", getIcon("warehouses"), "/WHS", new Locke[]{
                        new Locke("Find Warehouse", getIcon("find"), "/WHS/F", null),
                        new Locke("Create Warehouse", getIcon("create"), "/WHS/NEW", null),
                        new Locke("Modify Warehouse", getIcon("modify"), "/WHS/MOD", null),
                        new Locke("Archive Warehouse", getIcon("archive"), "/WHS/ARCHV", null),
                        new Locke("Remove Warehouse", getIcon("delete"), "/WHS/DEL", null),
                }),
                new Locke("Areas", getIcon("areas"), "/AREAS", new Locke[]{
                        new Locke("Find Area", getIcon("find"), "/AREAS/F", null),
                        new Locke("AutoMake Areas", getIcon("automake"), "/AREAS/AUTO_MK", null),
                        new Locke("Create Area", getIcon("create"), "/AREAS/NEW", null),
                        new Locke("Modify Area", getIcon("modify"), "/AREAS/MOD", null),
                        new Locke("Archive Area", getIcon("archive"), "/AREAS/ARCHV", null),
                        new Locke("Remove Area", getIcon("delete"), "/AREAS/DEL", null),
                        new Locke("Bins", getIcon("bins"), "/BNS", new Locke[]{
                            new Locke("Find Bin", getIcon("find"), "/BNS/F", null),
                            new Locke("AutoMake Bins", getIcon("automake"), "/BNS/AUTO_MK", null),
                            new Locke("Create Bin", getIcon("create"), "/BNS/NEW", null),
                            new Locke("Modify Bin", getIcon("modify"), "/BNS/MOD", null),
                            new Locke("Empty Bin", getIcon("locke"), "/BNS/EMPTY", null),
                            new Locke("Archive Bin", getIcon("archive"), "/BNS/ARCHV", null),
                            new Locke("Remove Bin", getIcon("delete"), "/BNS/DEL", null),
                        }),
                }),
                new Locke("Customers", getIcon("customers"), "/CSTS", new Locke[]{
                        new Locke("Find Customer", getIcon("find"), "/CSTS/F", null),
                        new Locke("Create Customer", getIcon("create"), "/CSTS/NEW", null),
                        new Locke("Modify Customer", getIcon("modify"), "/CSTS/MOD", null),
                        new Locke("Archive Customer", getIcon("archive"), "/CSTS/ARCHV", null),
                        new Locke("Remove Customer", getIcon("delete"), "/CSTS/DEL", null),
                }),
                new Locke("Accounts", getIcon("accounts"), "/ACCS", new Locke[]{
                        new Locke("Find Account", getIcon("find"), "/ACCS/F", null),
                        new Locke("Create Account", getIcon("create"), "/ACCS/NEW", null),
                        new Locke("Modify Account", getIcon("modify"), "/ACCS/MOD", null),
                        new Locke("Archive Account", getIcon("archive"), "/ACCS/ARCHV", null),
                        new Locke("Remove Account", getIcon("delete"), "/ACCS/DEL", null),
                }),
                new Locke("Invoices", getIcon("invoices"), "/INVS", new Locke[]{
                        new Locke("Find an Invoice", getIcon("find"), "/INVS/F", null),
                        new Locke("Create an Invoice", getIcon("create"), "/INVS/NEW", null),
                        new Locke("Accept Payment", getIcon("locke"), "/FIN/PYMNTS/NEW", null),
                        new Locke("Modify", getIcon("modify"), "/INVS/MOD", new Locke[]{
                                new Locke("Invoice", getIcon("invoices"), "/INVS/MOD", null),
                                new Locke("Credit", getIcon("locke"), "/INVS/CM/MOD", null),
                                new Locke("Debit", getIcon("locke"), "/INVS/DBT/MOD", null),
                        }),
                }),
                new Locke("Vendors", getIcon("vendors"), "/VEND", new Locke[]{
                        new Locke("Find Vendor", getIcon("find"), "/VEND/F", null),
                        new Locke("Create Vendor", getIcon("create"), "/VEND/NEW", null),
                        new Locke("Modify Vendor", getIcon("modify"), "/VEND/MOD", null),
                        new Locke("Block Vendor", getIcon("locke"), "", null),
                        new Locke("Archive Vendor", getIcon("archive"), "/VEND/ARCHV", null),
                        new Locke("Remove Vendor", getIcon("delete"), "/VEND/DEL", null),
                }),
                new Locke("Items", getIcon("items"), "/ITS", new Locke[]{
                        new Locke("Find Item", getIcon("find"), "/ITS/F", null),
                        new Locke("Create Item", getIcon("create"), "/ITS/NEW", null),
                        new Locke("Modify Item", getIcon("modify"), "/ITS/MOD", null),
                        new Locke("Archive Item", getIcon("archive"), "/ITS/ARCHV", null),
                        new Locke("Remove Item", getIcon("delete"), "/ITS/DEL", null),
                }),
                new Locke("Materials", getIcon("materials"), "/MTS", new Locke[]{
                        new Locke("Find Material", getIcon("find"), "/MTS/F", null),
                        new Locke("Create Material", getIcon("create"), "/MTS/NEW", null),
                        new Locke("Modify Material", getIcon("modify"), "/MTS/MOD", null),
                        new Locke("Archive Material", getIcon("archive"), "/MTS/ARCHV", null),
                        new Locke("Remove Material", getIcon("delete"), "/MTS/DEL", null),
                }),
                new Locke("Components", getIcon("components"), "/CMPS", new Locke[]{
                        new Locke("Find Component", getIcon("find"), "/CMPS/F", null),
                        new Locke("Create Component", getIcon("create"), "/CMPS/NEW", null),
                        new Locke("Modify Component", getIcon("modify"), "/CMPS/MOD", null),
                        new Locke("Archive Component", getIcon("archive"), "/CMPS/ARCHV", null),
                        new Locke("Remove Component", getIcon("delete"), "/CMPS/DEL", null),
                }),
                new Locke("VAS", getIcon("vas"), "/VAS", new Locke[]{
                        new Locke("Find VAS", getIcon("find"), "/VAS/F", null),
                        new Locke("Create VAS", getIcon("create"), "/VAS/NEW", null),
                        new Locke("Modify VAS", getIcon("modify"), "/VAS/MOD", null),
                        new Locke("Archive VAS", getIcon("archive"), "/VAS/ARCHV", null),
                        new Locke("Remove VAS", getIcon("delete"), "/VAS/DEL", null),
                }),
                new Locke("Employees", getIcon("employees"), "/EMPS", new Locke[]{
                        new Locke("Find Employee", getIcon("find"), "/EMPS/F", null),
                        new Locke("Create Employee", getIcon("create"), "/EMPS/NEW", null),
                        new Locke("Modify Employee", getIcon("modify"), "/EMPS/MOD", null),
                        new Locke("Archive Employee", getIcon("archive"), "/EMPS/ARCHV", null),
                        new Locke("Remove Employee", getIcon("delete"), "/EMPS/DEL", null),
                }),
                new Locke("Catalogs", getIcon("catalogs"), "/CATS", new Locke[]{
                        new Locke("Find Catalog", getIcon("find"), "/CATS/F", null),
                        new Locke("Create Catalog", getIcon("create"), "/CATS/NEW", null),
                        new Locke("Modify Catalog", getIcon("modify"), "/CATS/MOD", null),
                        new Locke("Archive Catalog", getIcon("archive"), "/CATS/ARCHV", null),
                        new Locke("Remove Catalog", getIcon("delete"), "/CATS", null),
                }),
                new Locke("Orders", getIcon("order"), "/ORDS/PO", new Locke[]{
                        new Locke("Purchase Orders", getIcon("order"), "/ORDS/PO", new Locke[]{
                                new Locke("Find Purchase Order", getIcon("find"), "/ORDS/PO/F", null),
                                new Locke("Create Purchase Order", getIcon("create"), "/ORDS/PO/NEW", null),
                                new Locke("Modify Purchase Order", getIcon("modify"), "/ORDS/PO/MOD", null),
                                new Locke("Block Purchase Order", getIcon("locke"), "/ORDS/PO/BLK", null),
                                new Locke("Suspend Purchase Order", getIcon("locke"), "/ORDS/PO/SP", null),
                                new Locke("Archive Purchase Order", getIcon("archive"), "/ORDS/PO/ARCHV", null),
                                new Locke("Remove Purchase Order", getIcon("delete"), "/ORDS/PO/DEL", null),
                        }),
                        new Locke("Purchase Reqs.", getIcon("order"), "/ORDS/PR", new Locke[]{
                                new Locke("AutoMake PRs", getIcon("automake"), "/ORDS/PR/AUTO_MK", null),
                                new Locke("Find PR", getIcon("find"), "/ORDS/PR", null),
                                new Locke("Create PR", getIcon("create"), "/ORDS/PR/NEW", null),
                                new Locke("Modify PR", getIcon("modify"), "/ORDS/PR/MOD", null),
                                new Locke("Block PR", getIcon("locke"), "/ORDS/PR/BLK", null),
                                new Locke("Suspend PR", getIcon("locke"), "/ORDS/PR/SP", null),
                                new Locke("Archive PR", getIcon("archive"), "/ORDS/PR/ARCHV", null),
                                new Locke("Remove PR", getIcon("delete"), "/ORDS/PR/DEL", null),
                        }),
                        new Locke("Sales Orders", getIcon("salesorders"), "/ORDS/SO", new Locke[]{ //Controller
                                new Locke("Find Sales Order", getIcon("find"), "/ORDS/SO", null),
                                new Locke("Create Sales Order", getIcon("create"), "/ORDS/SO/NEW", null),
                                new Locke("Modify Sales Order", getIcon("modify"), "/ORDS/SO/MOD", null),
                                new Locke("Block Sales Order", getIcon("locke"), "/ORDS/SO/BLK", null),
                                new Locke("Suspend Sales Order", getIcon("locke"), "/ORDS/SO/SP", null),
                                new Locke("Archive Sales Order", getIcon("archive"), "/ORDS/SO/ARCHV", null),
                                new Locke("Remove Sales Order", getIcon("delete"), "/ORDS/SO/DEL", null),
                        }),
                }),
                new Locke("Movements", getIcon("locke"), "/MVMT", new Locke[]{ //Controller
                        new Locke("Waves", getIcon("locke"), "/MVMT/WVS", new Locke[]{
                                new Locke("Find a Wave", getIcon("find"), "/MVMT/WVS/F", null),
                                new Locke("Create a Wave", getIcon("create"), "/MVMT/WVS/NEW", null),
                                new Locke("Modify a Wave", getIcon("modify"), "/MVMT/WVS/MOD", null),
                                new Locke("Pause a Wave", getIcon("archive"), "/MVMT/WVS/PS", null),
                                new Locke("Suspend a Wave", getIcon("archive"), "/MVMT/WVS/SSPND", null),
                                new Locke("Archive a Wave", getIcon("archive"), "/MVMT/WVS/ARCHV", null),
                                new Locke("Remove a Wave", getIcon("delete"), "/MVMT/WVS/DEL", null),
                        }),
                        new Locke("Flows", getIcon("locke"), "/MVMT/FLWS", new Locke[]{ //Controller
                                new Locke("Find a Flow", getIcon("find"), "/MVMT/FLWS/F", null),
                                new Locke("Create a Flow", getIcon("create"), "/MVMT/FLWS/NEW", null),
                                new Locke("Modify a Flow", getIcon("modify"), "/MVMT/FLWS/MOD", null),
                                new Locke("Pause a Flow", getIcon("archive"), "/MVMT/FLWS/PS", null),
                                new Locke("Suspend a Flow", getIcon("archive"), "/MVMT/FLWS/SSPND", null),
                                new Locke("Archive a Flow", getIcon("archive"), "/MVMT/FLWS/ARCHV", null),
                                new Locke("Remove a Flow", getIcon("delete"), "/MVMT/FLWS/DEL", null),
                        }),
                        new Locke("Work Orders", getIcon("locke"), "/MVMT/WO", new Locke[]{ //Controller
                                new Locke("Find Work Order", getIcon("find"), "/MVMT/WO", null),
                                new Locke("Create Work Order", getIcon("create"), "/MVMT/WO/NEW", null),
                                new Locke("Modify Work Order", getIcon("modify"), "/MVMT/WO/MOD", null),
                                new Locke("Block Work Order", getIcon("locke"), "/MVMT/WO/BLK", null),
                                new Locke("Suspend Work Order", getIcon("locke"), "/MVMT/WO/SP", null),
                                new Locke("Archive Work Order", getIcon("archive"), "/MVMT/WO/ARCHV", null),
                                new Locke("Remove Work Order", getIcon("delete"), "/MVMT/WO/DEL", null),
                        }),
                        new Locke("Tasks", getIcon("locke"), "/MVMT/TSKS", new Locke[]{ //Controller
                                new Locke("AutoMake a Task", getIcon("automake"), "/MVMT/TSKS/AUTO_MK", null),
                                new Locke("Find a Task", getIcon("find"), "/MVMT/TSKS", null),
                                new Locke("Create a Task", getIcon("create"), "/MVMT/TSKS/NEW", null),
                                new Locke("Modify a Task", getIcon("modify"), "/MVMT/TSKS/MOD", null),
                                new Locke("Block a Task", getIcon("locke"), "/MVMT/TSKS/BLK", null),
                                new Locke("Suspend a Task", getIcon("locke"), "/MVMT/TSKS/SP", null),
                                new Locke("Archive a Task", getIcon("archive"), "/MVMT/TSKS/ARCHV", null),
                                new Locke("Remove a Task", getIcon("delete"), "/MVMT/TSKS/DEL", null),
                        }),
                }),
                new Locke("Planning", getIcon("planning"), "/PLA", new Locke[]{
                        new Locke("Forecast", getIcon("locke"), "/PLA/FRCST", null),
                        new Locke("Demand", getIcon("locke"), "/PLA/DMD", null),
                        new Locke("Plan STO", getIcon("locke"), "/PLA/STO", null),
                        new Locke("Archive Plan", getIcon("archive"), "/PLA/ARCHV", null),
                        new Locke("Remove Plan", getIcon("delete"), "/PLA/DEL", null),
                }),
                new Locke("Transportation", getIcon("transportation"), "/TRANS", new Locke[]{
                        new Locke("Carriers", getIcon("carriers"), "/TRANS/CRRS", new Locke[]{
                                new Locke("Find Carrier", getIcon("find"), "/TRANS/CRRS/F", null),
                                new Locke("Create Carrier", getIcon("create"), "/TRANS/CRRS/NEW", null),
                                new Locke("Modify Carrier", getIcon("modify"), "/TRANS/CRRS/MOD", null),
                                new Locke("Archive Carrier", getIcon("archive"), "/TRANS/CRRS/ARCHV", null),
                                new Locke("Remove Carrier", getIcon("delete"), "/TRANS/CRRS/DEL", null),
                        }),
                        new Locke("Trucks", getIcon("trucks"), "/TRANS/TRCKS", new Locke[]{
                                new Locke("Find Truck", getIcon("find"), "/TRANS/TRCKS/F", null),
                                new Locke("Create Truck", getIcon("create"), "/TRANS/TRCKS/NEW", null),
                                new Locke("Modify Truck", getIcon("modify"), "/TRANS/TRCKS/MOD", null),
                                new Locke("Archive Truck", getIcon("archive"), "/TRANS/TRCKS/ARCHV", null),
                                new Locke("Remove Truck", getIcon("delete"), "/TRANS/TRCKS/DEL", null),
                        }),
                        new Locke("Inbound Deliveries", getIcon("inbound"), "/TRANS/IDO", new Locke[]{
                                new Locke("Find Inbound Delivery", getIcon("find"), "/TRANS/IDO/F", null),
                                new Locke("Create Inbound Delivery", getIcon("create"), "/TRANS/IDO/NEW", null),
                                new Locke("Modify Inbound Delivery", getIcon("modify"), "/TRANS/IDO/MOD", null),
                                new Locke("Archive Inbound Delivery", getIcon("archive"), "/TRANS/IDO/ARCHV", null),
                                new Locke("Remove Inbound Delivery", getIcon("delete"), "/TRANS/IDO/DEL", null),
                        }),
                        new Locke("Outbound Deliveries", getIcon("outbound"), "/TRANS/ODO", new Locke[]{
                                new Locke("Find Outbound Delivery", getIcon("find"), "/TRANS/ODO/F", null),
                                new Locke("Create Outbound Delivery", getIcon("create"), "/TRANS/ODO/NEW", null),
                                new Locke("Modify Outbound Delivery", getIcon("modify"), "/TRANS/ODO/MOD", null),
                                new Locke("Archive Outbound Delivery", getIcon("archive"), "/TRANS/ODO/ARCHV", null),
                                new Locke("Remove Outbound Delivery", getIcon("delete"), "/TRANS/ODO/DEL", null),
                        }),
                        new Locke("Advanced Shipping Notices", getIcon("folder"), "/TRANS/ASN", new Locke[]{
                                new Locke("Find ASN", getIcon("find"), "/TRANS/ASN/F", null),
                                new Locke("Create ASN", getIcon("create"), "/TRANS/ASN/NEW", null),
                                new Locke("Modify ASN", getIcon("modify"), "/TRANS/ASN/MOD", null),
                                new Locke("Archive ASN", getIcon("archive"), "/TRANS/ASN/ARCHV", null),
                                new Locke("Remove ASN", getIcon("delete"), "/TRANS/ASN/DEL", null),
                        }),
                }),
                new Locke("Ledgers", getIcon("ledgers"), "/LGS", new Locke[]{
                        new Locke("Find Ledger", getIcon("find"), "/LGS/F", null),
                        new Locke("Create Ledger", getIcon("create"), "/LGS/NEW", null),
                        new Locke("Modify Ledger", getIcon("modify"), "/LGS/MOD", null),
                        new Locke("Audit Ledger", getIcon("locke"), "/LGS/AUDIT", null),
                        new Locke("Archive Ledger", getIcon("archive"), "/LGS/ARCHV", null),
                        new Locke("Remove Ledger", getIcon("delete"), "/LGS/DEL", null),
                }),
                new Locke("Inventory", getIcon("inventory"), "/STK", new Locke[]{
                        new Locke("Stock Check", getIcon("locke"), "/STK/SC/", null),
                        new Locke("Org Stock Overview", getIcon("locke"), "/STK/SC/IID", null),
                        new Locke("Stock by Vendor", getIcon("locke"), "/STK/SC/VEND", null),
                        new Locke("Stock by Vendor with Item", getIcon("locke"), "/STK/SC/VEND_IID", null),
                        new Locke("Stock by Vendor with Cost Center", getIcon("locke"), "/STK/SC/VEND_CCS", null),
                        new Locke("Stock by Vendor for Distribution Center", getIcon("locke"), "/STK/SC/VEND_DCSS", null),
                        new Locke("Item Stock Check", getIcon("locke"), "/STK/SC/IID", null),
                        new Locke("Move Inventory", getIcon("locke"), "/STK/MOD/MV", null),
                        new Locke("Perform Stock Transfer Order (STO)", getIcon("locke"), "/STK/MV/STO", null),
                        new Locke("Remove Inventory", getIcon("locke"), "/STK/MV/DEL", null),
                        new Locke("Adjust Inventory", getIcon("locke"), "/STK/MV/ADJ", null),
                        new Locke("Physical Inventory", getIcon("locke"), "/STK/PI", null),
                        new Locke("Physical Inventory Count", getIcon("locke"), "/STK/PI_COUNT", null),
                        new Locke("Physical Inventory for Item", getIcon("locke"), "/STK/PI/ITS", null),
                        new Locke("Physical Inventory for Material", getIcon("locke"), "/STK/PI/MTS", null),
                        new Locke("Physical Inventory for Plant", getIcon("locke"), "/STK/PI/PLNT", null),
                }),
                new Locke("Users", getIcon("users"), "/USRS", new Locke[]{
                        new Locke("Find User", getIcon("find"), "/USRS/F", null),
                        new Locke("Create a User", getIcon("create"), "/USRS/NEW", null),
                        new Locke("View User", getIcon("locke"), "/USRS", null),
                        new Locke("Archive User", getIcon("archive"), "/USRS/ARCHV", null),
                        new Locke("Remove", getIcon("delete"), "/USRS/DEL", null),
                        new Locke("Modify a User", getIcon("modify"), "/USRS/MOD", new Locke[]{
                                new Locke("Suspend", getIcon("locke"), "/USRS/MOD/SP", null),
                                new Locke("Change Access", getIcon("locke"), "/USRS/MOD/CHGAC", null),
                        }),
                }),
                new Locke("Records", getIcon("folder"), "/RCS", new Locke[]{
                        new Locke("Find Record", getIcon("locke"), "/RCS/F", null),
                        new Locke("Export Records", getIcon("locke"), "/RCS/EXPORT", null),
                }),
        });
    }

    public static boolean isCanalAssigned(){
        return Engine.assignedUser != null;
    }

    public static void checkLocke(JInternalFrame parent, boolean checkLedgers, boolean checkOperator){
        if(!isCanalAssigned()){
            JOptionPane.showMessageDialog(parent, "Canal locked", "There is no user signed in so nothing can be created.", JOptionPane.INFORMATION_MESSAGE);
            try {
                parent.setClosed(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }else{
            if(checkLedgers){
                if(Engine.getLedgers().isEmpty()){
                    JOptionPane.showMessageDialog(parent, "No ledgers to order to.");
                    parent.dispose();
                }else{
                    if(Engine.getLocations("VEND").isEmpty()){
                        JOptionPane.showMessageDialog(parent, "No vendors to order from.");
                        parent.dispose();
                    }
                }
            }
        }
    }

    private static Icon getIcon(String icon) {
        ImageIcon imageIcon = new ImageIcon(Locke.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = imageIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Returns the standard format timestamp of this moment.
     * @return String Timestamp of now.
     */
    public static String now(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * Formats a double value as a USD currency string with three decimal places.
     *
     * @param amount The double value to format.
     * @return A string formatted as USD with three decimal places.
     */
    public static String formatUSD(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(3);
        return formatter.format(amount);
    }
}