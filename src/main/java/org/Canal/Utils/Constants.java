package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
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

    public static ArrayList<String> getAllTransactions() {
        ArrayList<String> transactions = new ArrayList<>();
        if (allModules().getTransaction() != null && !transactions.contains(allModules().getTransaction())) {
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
        if (canal.getTransaction() != null && !transactions.contains(canal.getTransaction())) {
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
        return new Locke("Canal – All Actions", true, "/ORGS", new Locke[]{
                new Locke("Quick Accesses", true, "/", new Locke[]{
                        new Locke("Finance", false, "/FIN", null),
                        new Locke("Inventory", false, "/INV", null),
                        new Locke("Human Resources", false, "/CNL/HR", null),
                        new Locke("Time Clock", false, "/TM_CLCK", null),
                        new Locke("Clear Desk", false, "/CLEAR_DSK", null),
                        new Locke("Close Desk", false, "/CLOSE_DSK", null),
                        new Locke("Canal Settings", false, "/CNL", null),
                        new Locke("Leave Canal", false, "/CNL/EXIT", null),
                }),
                new Locke("Organizations", true, "/ORGS", new Locke[]{
                        new Locke("Find Organization", false, "/ORGS/F", null),
                        new Locke("Create an Organization", false, "/ORGS/NEW", null),
                        new Locke("Modify an Organization", false, "/ORGS/MOD", null),
                        new Locke("Departments", true, "/DPTS",  new Locke[]{
                                new Locke("Find Department with ID", false, "/DPTS/F", null),
                                new Locke("Create a Department", false, "/DPTS/NEW", null),
                                new Locke("Modify Department", false, "/DPTS/MOD", null),
                                new Locke("Remove Department", false, "/DPTS/DEL", null),
                                new Locke("Archive Department", false, "/DPTS/ARCHV", null),
                        }),
                        new Locke("Positions", true, "/HR/POS",  new Locke[]{
                                new Locke("Find Position with ID", false, "/HR/POS/F", null),
                                new Locke("Create a Position", false, "/HR/POS/NEW", null),
                                new Locke("Modify Position", false, "/HR/POS/MOD", null),
                                new Locke("Remove Position", false, "/HR/POS/DEL", null),
                                new Locke("Archive Position", false, "/HR/POS/ARCHV", null),
                                new Locke("Post Position", false, "/HR/POS/POST", null),
                        }),
                }),
                new Locke("Cost Centers", true, "/CCS", new Locke[]{
                        new Locke("Find CC with ID", false, "/CCS/F", null),
                        new Locke("Create a Cost Center", false, "/CCS/NEW", null),
                        new Locke("Modify Cost Center", false, "/CCS/MOD", null),
                        new Locke("Remove Cost Center", false, "/CCS/DEL", null),
                        new Locke("Archive Cost Center", false, "/CCS/ARCHV", null),
                }),
                new Locke("Distribution Centers", true, "/DCSS", new Locke[]{
                        new Locke("Find with ID", false, "/DCSS/F", null),
                        new Locke("Create Dist. Center", false, "/DCSS/NEW", null),
                        new Locke("Modify Dist. Center", false, "/DCSS/MOD", null),
                        new Locke("Remove Dist. Center", false, "/DCSS/DEL", null),
                }),
                new Locke("Warehouses", true, "/WHS", new Locke[]{
                        new Locke("Find Warehouse", false, "/WHS/F", null),
                        new Locke("Create Warehouse", false, "/WHS/NEW", null),
                        new Locke("Modify Warehouse", false, "/WHS/MOD", null),
                        new Locke("Remove Warehouse", false, "/WHS/DEL", null),
                }),
                new Locke("Customers", true, "/CSTS", new Locke[]{
                        new Locke("Find Customer", false, "/CSTS/F", null),
                        new Locke("Create Customer", false, "/CSTS/NEW", null),
                        new Locke("Remove Customer", false, "/CSTS/DEL", null),
                        new Locke("Modify Customer", true, "/CSTS/MOD", new Locke[]{
                                new Locke("Invoice", false, "/CSTS/INVS/NEW", null),
                                new Locke("Delinquent", false, "/CSTS/DLQ", null),
                                new Locke("Block", false, "/CSTS/BLCK", null),
                                new Locke("Block", false, "/CSTS/ARCHV", null),
                        }),
                }),
                new Locke("Invoices", true, "/INVS", new Locke[]{
                        new Locke("Find", false, "/INVS/F", null),
                        new Locke("Create an Invoice", false, "/INVS/NEW", null),
                        new Locke("Accept Payment", false, "/FIN/PYMNTS/NEW", null),
                        new Locke("Modify", true, "/INVS/MOD", new Locke[]{
                                new Locke("Invoice", false, "/INVS/MOD", null),
                                new Locke("Credit", false, "/INVS/CM/MOD", null),
                                new Locke("Debit", false, "/INVS/DBT/MOD", null),
                        }),
                }),
                new Locke("Users", true, "/USRS", new Locke[]{
                        new Locke("Find User", false, "/USRS/F", null),
                        new Locke("Create a User", false, "/USRS/NEW", null),
                        new Locke("View User", false, "/USRS", null),
                        new Locke("Modify a User", true, "/USRS/MOD", new Locke[]{
                                new Locke("Suspend", false, "/USRS/MOD/SP", null),
                                new Locke("Change Access", false, "/USRS/MOD/CHGAC", null),
                                new Locke("Remove", false, "/USRS/MOD/DEL", null),
                        }),
                }),
                new Locke("Vendors", true, "/VEND", new Locke[]{
                        new Locke("Find Vendor", false, "/VEND/F", null),
                        new Locke("Create Vendor", false, "/VEND/NEW", null),
                        new Locke("Remove Vendor", false, "/VEND/DEL", null),
                        new Locke("Modify Vendor", true, "/VEND/MOD", null),
                        new Locke("Order from Vendor", false, "/VEND/ORDS/NEW", null),
                        new Locke("Vendor Stock Check", false, "/VEND/INV/CHK", null),
                        new Locke("Block Vendor", false, "", null)
                }),
                new Locke("Items", true, "/ITS", new Locke[]{
                        new Locke("Find Item", false, "/ITS/F", null),
                        new Locke("Create Item", false, "/ITS/NEW", null),
                        new Locke("Modify Item", false, "/ITS/MOD", null),
                        new Locke("Remove Item", false, "/ITS/DEL", null),
                }),
                new Locke("Materials", true, "/MTS", new Locke[]{
                        new Locke("Find Material", false, "/MTS/F", null),
                        new Locke("Create Material", false, "/MTS/NEW", null),
                        new Locke("Modify Material", false, "/MTS/MOD", null),
                        new Locke("Remove Material", false, "/MTS/DEL", null),
                }),
                new Locke("Employees", true, "/EMPS", new Locke[]{
                        new Locke("Find Employee", false, "/EMPS/F", null),
                        new Locke("Create Employee", false, "/EMPS/NEW", null),
                        new Locke("Modify Employee", false, "/EMPS/MOD", null),
                        new Locke("Remove Employee", false, "/EMPS/DEL", null),
                }),
                new Locke("Catalogs", true, "/CATS", new Locke[]{
                        new Locke("Find Catalog", false, "/CATS/F", null),
                        new Locke("Create Catalog", false, "/CATS/NEW", null),
                        new Locke("Modify Catalog", false, "/CATS", null),
                        new Locke("Remove Catalog", false, "/CATS", null),
                        new Locke("Archive Catalog", false, "/CATS/ARCHV", null),
                }),
                new Locke("Orders", true, "/ORDS", new Locke[]{
                        new Locke("Purchase Orders", true, "/ORDS", new Locke[]{
                                new Locke("AutoMake PRs", false, "/ORDS/PO/AUTO_MK", null),
                                new Locke("Find PO", false, "/ORDS/F", null),
                                new Locke("Create PO", false, "/ORDS/NEW", null),
                                new Locke("Remove PO", false, "/ORDS/DEL", null),
                                new Locke("Block PO", false, "/ORDS/BLK", null),
                                new Locke("Suspend PO", false, "/ORDS/SP", null),
                                new Locke("Archive PO", false, "/ORDS/ARCHV", null),
                        }),
                        new Locke("Purchase Reqs.", true, "/ORDS/PR", new Locke[]{
                                new Locke("AutoMake PRs", false, "/ORDS/PR/AUTO_MK", null),
                                new Locke("Find PR", false, "/ORDS/PR", null),
                                new Locke("Create PR", false, "/ORDS/PR/NEW", null),
                                new Locke("Remove PR", false, "/ORDS/PR/DEL", null),
                                new Locke("Block PR", false, "/ORDS/PR/BLK", null),
                                new Locke("Suspend PR", false, "/ORDS/PR/SP", null),
                                new Locke("Archive PR", false, "/ORDS/PR/ARCHV", null),
                        }),
                        new Locke("Sales Orders", true, "/ORDS/SO", new Locke[]{
                                new Locke("AutoMake PRs", false, "/ORDS/SO/AUTO_MK", null),
                                new Locke("Find SO", false, "/ORDS/SO", null),
                                new Locke("Create SO", false, "/ORDS/SO/NEW", null),
                                new Locke("Modify SO", false, "/ORDS/SO/MOD", null),
                                new Locke("Remove SO", false, "/ORDS/SO/DEL", null),
                                new Locke("Block SO", false, "/ORDS/SO/BLK", null),
                                new Locke("Suspend SO", false, "/ORDS/SO/SP", null),
                                new Locke("Archive SO", false, "/ORDS/SO/ARCHV", null),
                        }),
                }),
                new Locke("Planning", true, "/PLA", new Locke[]{
                        new Locke("Forecast", false, "/PLA/", null),
                        new Locke("Demand", false, "/PLA/", null),
                        new Locke("STO", false, "/PLA/", null),
                        new Locke("Remove", false, "/PLA/", null),
                }),
                new Locke("Distribution", true, "/DIST/", new Locke[]{
                        new Locke("Find", false, "/DIST/", null),
                        new Locke("Create", false, "/DIST/", null),
                        new Locke("Modify", false, "/DIST/", null),
                        new Locke("Remove", false, "/DIST/", null),
                }),
                new Locke("Ledgers", true, "/LGS", new Locke[]{
                        new Locke("Find", false, "/LGS/F", null),
                        new Locke("Create", false, "/LGS/NEW", null),
                        new Locke("Audit", false, "/LGS/AUDIT", null),
                        new Locke("Archive", false, "/LGS/ARCHV", null),
                }),
                new Locke("Inventory", true, "/INV", new Locke[]{
                        new Locke("Stock Check", true, "/INV/SC/", new Locke[]{
                                new Locke("Org Stock Overview", false, "/INV/SC/IID", null),
                                new Locke("Stock by Vendor", false, "/INV/SC/VEND", null),
                                new Locke("Stock by Vendor with Item", false, "/INV/SC/VEND_IID", null),
                                new Locke("Stock by Vendor with Cost Center", false, "/INV/SC/VEND_CCS", null),
                                new Locke("Stock by Vendor for Distribution Center", false, "/INV/SC/VEND_DCSS", null),
                                new Locke("Item Stock Check", false, "/INV/SC/IID", null),
                        }),
                        new Locke("Move Inventory", true, "/INV/MV/MOD", new Locke[]{
                                new Locke("Perform Stock Transfer Order (STO)", false, "/INV/MV/STO", null),
                        }),
                        new Locke("Remove Inventor", false, "/INV/MV/DEL", null),
                        new Locke("Adjust Inventory", false, "/INV/MV/ADJ", null),
                        new Locke("Physical Inventory", true, "/INV/PI", new Locke[]{
                                new Locke("Physical Inventory for Item", false, "/INV/PI/ITS", null),
                                new Locke("Physical Inventory for Material", false, "/INV/PI/MTS", null),
                                new Locke("Physical Inventory for Plant", false, "/INV/PI/PLNT", null),
                        }),
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

    public static String now(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}