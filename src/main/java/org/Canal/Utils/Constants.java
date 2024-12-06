package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
                        new Locke("Finance", getIcon("locke"), "/FIN", null),
                        new Locke("Inventory", getIcon("locke"), "/INV", null),
                        new Locke("Human Resources", getIcon("locke"), "/CNL/HR", null),
                        new Locke("Time Clock", getIcon("locke"), "/TM_CLCK", null),
                        new Locke("Clear Desk", getIcon("locke"), "/CLEAR_DSK", null),
                        new Locke("Close Desk", getIcon("locke"), "/CLOSE_DSK", null),
                        new Locke("Canal Settings", getIcon("locke"), "/CNL", null),
                        new Locke("Leave Canal", getIcon("locke"), "/CNL/EXIT", null),
                }),
                new Locke("Organizations", getIcon("organizations"), "/ORGS", new Locke[]{
                        new Locke("Find Organization", getIcon("locke"), "/ORGS/F", null),
                        new Locke("Create an Organization", getIcon("locke"), "/ORGS/NEW", null),
                        new Locke("Modify an Organization", getIcon("locke"), "/ORGS/MOD", null),
                        new Locke("Departments", getIcon("departments"), "/DPTS",  new Locke[]{
                                new Locke("Find Department with ID", getIcon("locke"), "/DPTS/F", null),
                                new Locke("Create a Department", getIcon("locke"), "/DPTS/NEW", null),
                                new Locke("Modify Department", getIcon("locke"), "/DPTS/MOD", null),
                                new Locke("Remove Department", getIcon("locke"), "/DPTS/DEL", null),
                                new Locke("Archive Department", getIcon("locke"), "/DPTS/ARCHV", null),
                        }),
                        new Locke("Positions", getIcon("positions"), "/HR/POS",  new Locke[]{
                                new Locke("Find Position with ID", getIcon("locke"), "/HR/POS/F", null),
                                new Locke("Create a Position", getIcon("locke"), "/HR/POS/NEW", null),
                                new Locke("Modify Position", getIcon("locke"), "/HR/POS/MOD", null),
                                new Locke("Remove Position", getIcon("locke"), "/HR/POS/DEL", null),
                                new Locke("Archive Position", getIcon("locke"), "/HR/POS/ARCHV", null),
                                new Locke("Post Position", getIcon("locke"), "/HR/POS/POST", null),
                        }),
                        new Locke("Teams", getIcon("teams"), "/TMS",  new Locke[]{
                                new Locke("Find Team with ID", getIcon("locke"), "/TMS/F", null),
                                new Locke("Create a Team", getIcon("locke"), "/TMS/NEW", null),
                                new Locke("Modify Team", getIcon("locke"), "/TMS/MOD", null),
                                new Locke("Remove Team", getIcon("locke"), "/TMS/DEL", null),
                                new Locke("Archive Team", getIcon("locke"), "/TMS/ARCHV", null),
                        }),
                }),
                new Locke("Cost Centers", getIcon("cost_centers"), "/CCS", new Locke[]{
                        new Locke("Find CC with ID", getIcon("locke"), "/CCS/F", null),
                        new Locke("Create a Cost Center", getIcon("locke"), "/CCS/NEW", null),
                        new Locke("Modify Cost Center", getIcon("locke"), "/CCS/MOD", null),
                        new Locke("Remove Cost Center", getIcon("locke"), "/CCS/DEL", null),
                        new Locke("Archive Cost Center", getIcon("locke"), "/CCS/ARCHV", null),
                }),
                new Locke("Distribution Centers", getIcon("distribution_centers"), "/DCSS", new Locke[]{
                        new Locke("Find with ID", getIcon("locke"), "/DCSS/F", null),
                        new Locke("Create Dist. Center", getIcon("locke"), "/DCSS/NEW", null),
                        new Locke("Modify Dist. Center", getIcon("locke"), "/DCSS/MOD", null),
                        new Locke("Remove Dist. Center", getIcon("locke"), "/DCSS/DEL", null),
                }),
                new Locke("Warehouses", getIcon("warehouses"), "/WHS", new Locke[]{
                        new Locke("Find Warehouse", getIcon("locke"), "/WHS/F", null),
                        new Locke("Create Warehouse", getIcon("locke"), "/WHS/NEW", null),
                        new Locke("Modify Warehouse", getIcon("locke"), "/WHS/MOD", null),
                        new Locke("Remove Warehouse", getIcon("locke"), "/WHS/DEL", null),
                }),
                new Locke("Areas", getIcon("areas"), "/AREAS", new Locke[]{
                        new Locke("Find Area", getIcon("locke"), "/AREAS/F", null),
                        new Locke("AutoMake Areas/Bins", getIcon("locke"), "/AREAS/AUTO_MK", null),
                        new Locke("Create Area", getIcon("locke"), "/AREAS/NEW", null),
                        new Locke("Modify Area", getIcon("locke"), "/AREAS/MOD", null),
                        new Locke("Remove Area", getIcon("locke"), "/AREAS/DEL", null),
                        new Locke("Archive Area", getIcon("locke"), "/AREAS/ARCHV", null),
                        new Locke("Bins", getIcon("bins"), "/BNS", new Locke[]{
                            new Locke("Find Bin", getIcon("locke"), "/BNS/F", null),
                            new Locke("Create Bin", getIcon("locke"), "/BNS/NEW", null),
                            new Locke("Modify Bin", getIcon("locke"), "/BNS/MOD", null),
                            new Locke("Remove Bin", getIcon("locke"), "/BNS/DEL", null),
                            new Locke("Archive Bin", getIcon("locke"), "/BNS/ARCHV", null),
                            new Locke("Empty Bin", getIcon("locke"), "/BNS/EMPTY", null),
                        }),
                }),
                new Locke("Customers", getIcon("customers"), "/CSTS", new Locke[]{
                        new Locke("Find Customer", getIcon("locke"), "/CSTS/F", null),
                        new Locke("Create Customer", getIcon("locke"), "/CSTS/NEW", null),
                        new Locke("Remove Customer", getIcon("locke"), "/CSTS/DEL", null),
                        new Locke("Modify Customer", UIManager.getIcon("FileView.directoryIcon"), "/CSTS/MOD", new Locke[]{
                                new Locke("Invoice", getIcon("locke"), "/CSTS/INVS/NEW", null),
                                new Locke("Delinquent", getIcon("locke"), "/CSTS/DLQ", null),
                                new Locke("Block", getIcon("locke"), "/CSTS/BLCK", null),
                                new Locke("Archive", getIcon("locke"), "/CSTS/ARCHV", null),
                        }),
                }),
                new Locke("Invoices", getIcon("invoices"), "/INVS", new Locke[]{
                        new Locke("Find", getIcon("locke"), "/INVS/F", null),
                        new Locke("Create an Invoice", getIcon("locke"), "/INVS/NEW", null),
                        new Locke("Accept Payment", getIcon("locke"), "/FIN/PYMNTS/NEW", null),
                        new Locke("Modify", UIManager.getIcon("FileView.directoryIcon"), "/INVS/MOD", new Locke[]{
                                new Locke("Invoice", getIcon("locke"), "/INVS/MOD", null),
                                new Locke("Credit", getIcon("locke"), "/INVS/CM/MOD", null),
                                new Locke("Debit", getIcon("locke"), "/INVS/DBT/MOD", null),
                        }),
                }),
                new Locke("Users", getIcon("users"), "/USRS", new Locke[]{
                        new Locke("Find User", getIcon("locke"), "/USRS/F", null),
                        new Locke("Create a User", getIcon("locke"), "/USRS/NEW", null),
                        new Locke("View User", getIcon("locke"), "/USRS", null),
                        new Locke("Modify a User", UIManager.getIcon("FileView.directoryIcon"), "/USRS/MOD", new Locke[]{
                                new Locke("Suspend", getIcon("locke"), "/USRS/MOD/SP", null),
                                new Locke("Change Access", getIcon("locke"), "/USRS/MOD/CHGAC", null),
                                new Locke("Remove", getIcon("locke"), "/USRS/MOD/DEL", null),
                        }),
                }),
                new Locke("Vendors", getIcon("vendors"), "/VEND", new Locke[]{
                        new Locke("Find Vendor", getIcon("locke"), "/VEND/F", null),
                        new Locke("Create Vendor", getIcon("locke"), "/VEND/NEW", null),
                        new Locke("Remove Vendor", getIcon("locke"), "/VEND/DEL", null),
                        new Locke("Modify Vendor", getIcon("locke"), "/VEND/MOD", null),
                        new Locke("Order from Vendor", getIcon("locke"), "/VEND/ORDS/NEW", null),
                        new Locke("Vendor Stock Check", getIcon("locke"), "/VEND/INV/CHK", null),
                        new Locke("Block Vendor", getIcon("locke"), "", null)
                }),
                new Locke("Items", getIcon("items"), "/ITS", new Locke[]{
                        new Locke("Find Item", getIcon("locke"), "/ITS/F", null),
                        new Locke("Create Item", getIcon("locke"), "/ITS/NEW", null),
                        new Locke("Modify Item", getIcon("locke"), "/ITS/MOD", null),
                        new Locke("Remove Item", getIcon("locke"), "/ITS/DEL", null),
                }),
                new Locke("Materials", getIcon("materials"), "/MTS", new Locke[]{
                        new Locke("Find Material", getIcon("locke"), "/MTS/F", null),
                        new Locke("Create Material", getIcon("locke"), "/MTS/NEW", null),
                        new Locke("Modify Material", getIcon("locke"), "/MTS/MOD", null),
                        new Locke("Remove Material", getIcon("locke"), "/MTS/DEL", null),
                }),
                new Locke("Components", getIcon("components"), "/CMPS", new Locke[]{
                        new Locke("Find Component", getIcon("locke"), "/CMPS/F", null),
                        new Locke("Create Component", getIcon("locke"), "/CMPS/NEW", null),
                        new Locke("Modify Component", getIcon("locke"), "/CMPS/MOD", null),
                        new Locke("Remove Component", getIcon("locke"), "/CMPS/DEL", null),
                }),
                new Locke("VAS", getIcon("vas"), "/VAS", new Locke[]{
                        new Locke("Find VAS", getIcon("locke"), "/VAS/F", null),
                        new Locke("Create VAS", getIcon("locke"), "/VAS/NEW", null),
                        new Locke("Modify VAS", getIcon("locke"), "/VAS/MOD", null),
                        new Locke("Remove VAS", getIcon("locke"), "/VAS/DEL", null),
                }),
                new Locke("Employees", getIcon("employees"), "/EMPS", new Locke[]{
                        new Locke("Find Employee", getIcon("locke"), "/EMPS/F", null),
                        new Locke("Create Employee", getIcon("locke"), "/EMPS/NEW", null),
                        new Locke("Modify Employee", getIcon("locke"), "/EMPS/MOD", null),
                        new Locke("Remove Employee", getIcon("locke"), "/EMPS/DEL", null),
                }),
                new Locke("Catalogs", getIcon("catalogs"), "/CATS", new Locke[]{
                        new Locke("Find Catalog", getIcon("locke"), "/CATS/F", null),
                        new Locke("Create Catalog", getIcon("locke"), "/CATS/NEW", null),
                        new Locke("Modify Catalog", getIcon("locke"), "/CATS", null),
                        new Locke("Remove Catalog", getIcon("locke"), "/CATS", null),
                        new Locke("Archive Catalog", getIcon("locke"), "/CATS/ARCHV", null),
                }),
                new Locke("Orders", getIcon("order"), "/ORDS", new Locke[]{
                        new Locke("Purchase Orders", getIcon("order"), "/ORDS", new Locke[]{
                                new Locke("AutoMake PRs", getIcon("locke"), "/ORDS/PO/AUTO_MK", null),
                                new Locke("Find PO", getIcon("locke"), "/ORDS/F", null),
                                new Locke("Create PO", getIcon("locke"), "/ORDS/NEW", null),
                                new Locke("Remove PO", getIcon("locke"), "/ORDS/DEL", null),
                                new Locke("Block PO", getIcon("locke"), "/ORDS/BLK", null),
                                new Locke("Suspend PO", getIcon("locke"), "/ORDS/SP", null),
                                new Locke("Archive PO", getIcon("locke"), "/ORDS/ARCHV", null),
                        }),
                        new Locke("Purchase Reqs.", getIcon("order"), "/ORDS/PR", new Locke[]{
                                new Locke("AutoMake PRs", getIcon("locke"), "/ORDS/PR/AUTO_MK", null),
                                new Locke("Find PR", getIcon("locke"), "/ORDS/PR", null),
                                new Locke("Create PR", getIcon("locke"), "/ORDS/PR/NEW", null),
                                new Locke("Remove PR", getIcon("locke"), "/ORDS/PR/DEL", null),
                                new Locke("Block PR", getIcon("locke"), "/ORDS/PR/BLK", null),
                                new Locke("Suspend PR", getIcon("locke"), "/ORDS/PR/SP", null),
                                new Locke("Archive PR", getIcon("locke"), "/ORDS/PR/ARCHV", null),
                        }),
                        new Locke("Sales Orders", getIcon("order"), "/ORDS/SO", new Locke[]{
                                new Locke("AutoMake PRs", getIcon("locke"), "/ORDS/SO/AUTO_MK", null),
                                new Locke("Find SO", getIcon("locke"), "/ORDS/SO", null),
                                new Locke("Create SO", getIcon("locke"), "/ORDS/SO/NEW", null),
                                new Locke("Modify SO", getIcon("locke"), "/ORDS/SO/MOD", null),
                                new Locke("Remove SO", getIcon("locke"), "/ORDS/SO/DEL", null),
                                new Locke("Block SO", getIcon("locke"), "/ORDS/SO/BLK", null),
                                new Locke("Suspend SO", getIcon("locke"), "/ORDS/SO/SP", null),
                                new Locke("Archive SO", getIcon("locke"), "/ORDS/SO/ARCHV", null),
                        }),
                }),
                new Locke("Planning", getIcon("planning"), "/PLA", new Locke[]{
                        new Locke("Forecast", getIcon("locke"), "/PLA/", null),
                        new Locke("Demand", getIcon("locke"), "/PLA/", null),
                        new Locke("STO", getIcon("locke"), "/PLA/", null),
                        new Locke("Remove", getIcon("locke"), "/PLA/", null),
                }),
                new Locke("Transportation", getIcon("transportation"), "/TRANS", new Locke[]{
                        new Locke("Carriers", getIcon("carriers"), "/TRANS/CRRS", new Locke[]{
                                new Locke("Find Carrier", getIcon("locke"), "/TRANS/CRRS/F", null),
                                new Locke("Create Carrier", getIcon("locke"), "/TRANS/CRRS/NEW", null),
                                new Locke("Modify Carrier", getIcon("locke"), "/TRANS/CRRS/MOD", null),
                                new Locke("Remove Carrier", getIcon("locke"), "/TRANS/CRRS/DEL", null),
                                new Locke("Archive Carrier", getIcon("locke"), "/TRANS/CRRS/ARCHV", null),
                        }),
                        new Locke("Inbound Deliveries", getIcon("inbound"), "/TRANS/IDO", new Locke[]{
                                new Locke("Find Inbound Delivery", getIcon("locke"), "/TRANS/IDO/F", null),
                                new Locke("Create Inbound Delivery", getIcon("locke"), "/TRANS/IDO/NEW", null),
                                new Locke("Modify Inbound Delivery", getIcon("locke"), "/TRANS/IDO/MOD", null),
                                new Locke("Remove Inbound Delivery", getIcon("locke"), "/TRANS/IDO/DEL", null),
                                new Locke("Archive Inbound Delivery", getIcon("locke"), "/TRANS/IDO/ARCHV", null),
                        }),
                        new Locke("Outbound Deliveries", getIcon("outbound"), "/TRANS/ODO", new Locke[]{
                                new Locke("Find Outbound Delivery", getIcon("locke"), "/TRANS/ODO/F", null),
                                new Locke("Create Outbound Delivery", getIcon("locke"), "/TRANS/ODO/NEW", null),
                                new Locke("Modify Outbound Delivery", getIcon("locke"), "/TRANS/ODO/MOD", null),
                                new Locke("Remove Outbound Delivery", getIcon("locke"), "/TRANS/ODO/DEL", null),
                                new Locke("Archive Outbound Delivery", getIcon("locke"), "/TRANS/ODO/ARCHV", null),
                        }),
                }),
                new Locke("Ledgers", getIcon("ledgers"), "/LGS", new Locke[]{
                        new Locke("Find", getIcon("locke"), "/LGS/F", null),
                        new Locke("Create", getIcon("locke"), "/LGS/NEW", null),
                        new Locke("Audit", getIcon("locke"), "/LGS/AUDIT", null),
                        new Locke("Archive", getIcon("locke"), "/LGS/ARCHV", null),
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
                })
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
                    if(Engine.getVendors().isEmpty()){
                        JOptionPane.showMessageDialog(parent, "No vendors to order from.");
                        parent.dispose();
                    }
                }
            }
        }
    }

    private static Icon getIcon(String icon) {
        ImageIcon imageIcon = new ImageIcon(Locke.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static String now(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}