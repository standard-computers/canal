package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;
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
            for (Canal child : allModules().getChildren()) {
                transactions.addAll(getTransactions(child));
            }
        }
        return removeDuplicates(transactions);
    }

    public static ArrayList<String> getTransactions(Canal canal) {
        ArrayList<String> transactions = new ArrayList<>();
        if (canal.getTransaction() != null && !transactions.contains(canal.getTransaction())) {
            transactions.add(canal.getTransaction());
        }
        if (canal.getChildren() != null) {
            for (Canal child : canal.getChildren()) {
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

    public static Canal allModules() {
        return new Canal("Canal – All Actions", true, "/ORGS", new Canal[]{
                new Canal("Quick Accesses", true, "/", new Canal[]{
                        new Canal("Finance", false, "/FIN", null),
                        new Canal("Inventory", false, "/INV", null),
                        new Canal("Time Clock", false, "/TM_CLCK", null),
                        new Canal("Inventory", false, "/INV", null),
                        new Canal("Canal Settings", false, "/CNL", null),
                        new Canal("Leave Canal", false, "/CNL/EXIT", null),
                }),
                new Canal("Organizations", true, "/ORGS", new Canal[]{
                        new Canal("Find Organization", false, "/ORGS/F", null),
                        new Canal("Create an Organization", false, "/ORGS/NEW", null),
                        new Canal("Modify an Organization", false, "/ORGS/MOD", null),
                }),
                new Canal("Cost Centers", true, "/CCS", new Canal[]{
                        new Canal("Find with ID", false, "/CCS/F", null),
                        new Canal("Create a Cost Center", false, "/CCS/NEW", null),
                        new Canal("Modify", false, "/CCS/MOD", null),
                        new Canal("Remove", false, "/CCS/DEL", null),
                }),
                new Canal("Distribution Centers", true, "/DCSS", new Canal[]{
                        new Canal("Find with ID", false, "/DCSS/F", null),
                        new Canal("Create", false, "/DCSS/NEW", null),
                        new Canal("Modify", false, "/DCSS/MOD", null),
                        new Canal("Remove", false, "/DCSS/DEL", null),
                }),
                new Canal("Warehouses", true, "/WHS", new Canal[]{
                        new Canal("Find", false, "/WHS/F", null),
                        new Canal("Create", false, "/WHS/NEW", null),
                        new Canal("Modify", false, "/WHS/MOD", null),
                        new Canal("Remove", false, "/WHS/DEL", null),
                }),
                new Canal("Customers", true, "/CSTS", new Canal[]{
                        new Canal("Find", false, "/CSTS/F", null),
                        new Canal("Create", false, "/CSTS/NEW", null),
                        new Canal("Remove", false, "/", null),
                        new Canal("Modify", true, "/", new Canal[]{
                                new Canal("Invoice", false, "/", null),
                                new Canal("Delinquent", false, "/", null),
                                new Canal("Block", false, "/", null),
                        }),
                }),
                new Canal("Invoicing", true, "/INVS", new Canal[]{
                        new Canal("Find", false, "/INVS/F", null),
                        new Canal("Create an Invoice", false, "/INVS/NEW", null),
                        new Canal("Modify", true, "/INVS/MOD", new Canal[]{
                                new Canal("Invoice", false, "/INVS/", null),
                        }),
                }),
                new Canal("Users", true, "/USRS", new Canal[]{
                        new Canal("Find", false, "/USRS/F", null),
                        new Canal("Create", false, "/USRS/NEW", null),
                        new Canal("View", false, "/USRS", null),
                        new Canal("Modify", true, "/USRS/MOD", new Canal[]{
                                new Canal("Suspend", false, "/USRS/MOD/SP", null),
                                new Canal("Change Access", false, "/USRS/MOD/CHGAC", null),
                                new Canal("Remove", false, "/USRS/MOD/DEL", null),
                        }),
                }),
                new Canal("Vendors", true, "/VEND", new Canal[]{
                        new Canal("Find", false, "/VEND/F", null),
                        new Canal("Create", false, "/VEND/NEW", null),
                        new Canal("Remove", false, "/VEND/DEL", null),
                        new Canal("Modify", true, "/VEND/MOD", new Canal[]{
                                new Canal("Order", false, "/ORDS/NEW", null),
                                new Canal("Stock Check", false, "", null),
                                new Canal("Block", false, "", null)
                        }),
                }),
                new Canal("Items", true, "/ITS", new Canal[]{
                        new Canal("Find", false, "/ITS/F", null),
                        new Canal("Create", false, "/ITS/NEW", null),
                        new Canal("Modify", false, "/ITS/MOD", null),
                        new Canal("Remove", false, "/ITS/DEL", null),
                }),
                new Canal("Materials", true, "/MTS", new Canal[]{
                        new Canal("Find", false, "/MTS/F", null),
                        new Canal("Create", false, "/MTS/NEW", null),
                        new Canal("Modify", false, "/MTS/MOD", null),
                        new Canal("Remove", false, "/MTS/DEL", null),
                }),
                new Canal("Employees", true, "/EMPS", new Canal[]{
                        new Canal("Find", false, "/EMPS/F", null),
                        new Canal("Create", false, "/EMPS/NEW", null),
                        new Canal("Modify", false, "/EMPS/MOD", null),
                        new Canal("Remove", false, "/EMPS/DEL", null),
                }),
                new Canal("Catalogs", true, "/CATS", new Canal[]{
                        new Canal("Find", false, "/CATS/F", null),
                        new Canal("Create", false, "/CATS/NEW", null),
                        new Canal("Modify", false, "/CATS", null),
                        new Canal("Remove", false, "/CATS", null),
                }),
                new Canal("Orders", true, "/ORDS", new Canal[]{

                        new Canal("Purchase Orders", true, "/ORDS", new Canal[]{
                                new Canal("Find", false, "/ORDS/F", null),
                                new Canal("Create", false, "/ORDS/NEW", null),
                                new Canal("Remove", false, "/ORDS/DEL", null),
                                new Canal("Block", false, "/ORDS/BLK", null),
                                new Canal("Suspend", false, "/ORDS/SP", null),
                                new Canal("Archive", false, "/ORDS/ARCHV", null),
                        }),
                        new Canal("Purchase Reqs.", true, "/ORDS/PR", new Canal[]{
                                new Canal("Find", false, "/ORDS/PR", null),
                                new Canal("Create", false, "/ORDS/PR/NEW", null),
                                new Canal("Remove", false, "/ORDS/PR/DEL", null),
                                new Canal("Block", false, "/ORDS/PR/BLK", null),
                                new Canal("Suspend", false, "/ORDS/PR/SP", null),
                                new Canal("Archive", false, "/ORDS/PR/ARCHV", null),
                        }),
                        new Canal("Sales Orders", true, "/ORDS/SO", new Canal[]{
                                new Canal("Find", false, "/ORDS/SO", null),
                                new Canal("Create", false, "/ORDS/SO/NEW", null),
                                new Canal("Modify", false, "/ORDS/SO/MOD", null),
                                new Canal("Remove", false, "/ORDS/SO/DEL", null),
                                new Canal("Block", false, "/ORDS/SO/BLK", null),
                                new Canal("Suspend", false, "/ORDS/SO/SP", null),
                                new Canal("Archive", false, "/ORDS/SO/ARCHV", null),
                        }),
                }),
                new Canal("Planning", true, "/PLA", new Canal[]{
                        new Canal("Forecast", false, "/", null),
                        new Canal("Demand", false, "/", null),
                        new Canal("STO", false, "/", null),
                        new Canal("Remove", false, "/", null),
                }),
                new Canal("Distribution", true, "/LGS", new Canal[]{
                        new Canal("Find", false, "/", null),
                        new Canal("Create", false, "/", null),
                        new Canal("Modify", false, "/", null),
                        new Canal("Remove", false, "/", null),
                }),
                new Canal("Ledgers", true, "/LGS", new Canal[]{
                        new Canal("Find", false, "/LGS/F", null),
                        new Canal("Create", false, "/LGS/NEW", null),
                        new Canal("Audit", false, "/", null),
                }),
                new Canal("Inventory", true, "/INV", new Canal[]{
                        new Canal("Stock Check", true, "/INV/SC/", new Canal[]{
                                new Canal("Org Stock Overview", false, "/INV/SC/IID", null),
                                new Canal("Stock by Vendor", false, "/INV/SC/VEND", null),
                                new Canal("Stock by Vendor with Item", false, "/INV/SC/VEND_IID", null),
                                new Canal("Stock by Vendor with Cost Center", false, "/INV/SC/VEND_CCS", null),
                                new Canal("Stock by Vendor for Distribution Center", false, "/INV/SC/VEND_DCSS", null),
                                new Canal("Item Stock Check", false, "/INV/SC/IID", null),
                        }),
                        new Canal("Move Inventory", true, "/INV/MV/MOD", new Canal[]{
                                new Canal("Perform Stock Transfer Order (STO)", false, "/INV/MV/STO", null),
                        }),
                        new Canal("Remove Inventor", false, "/INV/MV/DEL", null),
                        new Canal("Adjust Inventory", false, "/INV/MV/ADJ", null),
                        new Canal("Physical Inventory", true, "/INV/PI", new Canal[]{
                                new Canal("Physical Inventory for Item", false, "/INV/PI/ITS", null),
                                new Canal("Physical Inventory for Material", false, "/INV/PI/MTS", null),
                        }),
                })
        });
    }

    public static boolean isCanalAssigned(){
        return Engine.client == null;
    }

    public static void checkLocke(JInternalFrame parent, boolean checkLedgers){
        if(!isCanalAssigned()){
            JOptionPane.showMessageDialog(parent, "Canal locked", "There is no user signed in so nothing can be created.", JOptionPane.INFORMATION_MESSAGE);
            parent.dispose();
        }else{
            if(checkLedgers){
                if(Engine.getLedgers().size() == 0){
                    JOptionPane.showMessageDialog(parent, "No ledgers to order to.");
                    parent.dispose();
                }else{
                    if(Engine.getVendors().size() == 0){
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