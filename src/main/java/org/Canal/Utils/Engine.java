package org.Canal.Utils;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Views.Find.*;
import org.Canal.UI.Views.Lists.*;
import org.Canal.UI.Views.Modifiers.*;
import org.Canal.UI.Views.New.*;
import org.Canal.UI.Views.Singleton.*;
import org.Canal.UI.Views.Singleton.Managers.Finance;
import org.Canal.UI.Views.Singleton.Managers.Inventory;
import org.Canal.UI.Views.Singleton.Orders.AutoMakePurchaseRequisitions;
import org.Canal.UI.Views.Singleton.Orders.PurchaseOrders;
import org.Canal.UI.Views.Singleton.Orders.PurchaseRequisitions;
import org.Canal.UI.Views.Transactions.Distribution.ReceiveOrder;
import org.Canal.UI.Views.Transactions.Inventory.CreateSTO;
import org.Canal.UI.Views.Transactions.Inventory.InventoryForItem;
import org.Canal.UI.Views.Transactions.Inventory.InventoryForMaterial;
import org.Canal.UI.Views.Transactions.Invoices.CreateInvoice;
import org.Canal.UI.Views.Transactions.Orders.CreatePurchaseOrder;
import org.Canal.UI.Views.Transactions.Orders.CreatePurchaseRequisition;
import org.Canal.UI.Views.Transactions.Orders.CreateSalesOrder;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Engine {

    private static Configuration configuration;
    public static Organization organization;
    public static User client;
    public static ArrayList<Location> distributionCenters = new ArrayList<>();
    public static ArrayList<Warehouse> warehouses = new ArrayList<>();
    public static ArrayList<Location> customers = new ArrayList<>();
    public static ArrayList<Location> vendors = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>();
    public static ArrayList<Material> materials = new ArrayList<>();
    public static ArrayList<Area> areas = new ArrayList<>();
    public static ArrayList<Employee> employees = new ArrayList<>();
    public static ArrayList<Catalog> catalogs = new ArrayList<>();
    public static ArrayList<PurchaseOrder> orders = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Ledger> ledgers = new ArrayList<>();

    public static void load(){
        distributionCenters.clear();
        File[] dcssDir = Pipe.list("DCSS");
        for (File file : dcssDir) {
            if (!file.isDirectory()) {
                Location l = Json.load(file.getPath(), Location.class);
                distributionCenters.add(l);
            }
        }
        distributionCenters.sort(Comparator.comparing(Location::getId));
        warehouses.clear();
        File[] whdir = Pipe.list("WHS");
        for (File file : whdir) {
            if (!file.isDirectory()) {
                Warehouse l = Json.load(file.getPath(), Warehouse.class);
                warehouses.add(l);
            }
        }
        warehouses.sort(Comparator.comparing(Warehouse::getId));
        customers.clear();
        File[] cstsDir = Pipe.list("CSTS");
        for (File file : cstsDir) {
            if (!file.isDirectory()) {
                Location l = Json.load(file.getPath(), Location.class);
                customers.add(l);
            }
        }
        customers.sort(Comparator.comparing(Location::getId));
        vendors.clear();
        File[] vendDir = Pipe.list("VEND");
        for (File file : vendDir) {
            if (!file.isDirectory()) {
                Location l = Json.load(file.getPath(), Location.class);
                vendors.add(l);
            }
        }
        vendors.sort(Comparator.comparing(Location::getId));
        items.clear();
        File[] itsDir = Pipe.list("ITS");
        for (File file : itsDir) {
            if (!file.isDirectory()) {
                Item l = Json.load(file.getPath(), Item.class);
                items.add(l);
            }
        }
        items.sort(Comparator.comparing(Item::getId));
        areas.clear();
        File[] areasDir = Pipe.list("AREAS");
        for (File file : areasDir) {
            if (!file.isDirectory()) {
                Area a = Json.load(file.getPath(), Area.class);
                areas.add(a);
            }
        }
        areas.sort(Comparator.comparing(Area::getId));
        materials.clear();
        File[] mtsDir = Pipe.list("MTS");
        for (File file : mtsDir) {
            if (!file.isDirectory()) {
                Material a = Json.load(file.getPath(), Material.class);
                materials.add(a);
            }
        }
        materials.sort(Comparator.comparing(Material::getId));
        employees.clear();
        File[] empDir = Pipe.list("EMPS");
        for (File file : empDir) {
            if (!file.isDirectory()) {
                Employee a = Json.load(file.getPath(), Employee.class);
                employees.add(a);
            }
        }
        employees.sort(Comparator.comparing(Employee::getId));
        catalogs.clear();
        File[] catsDirs = Pipe.list("CATS");
        for (File catsDir : catsDirs) {
            if (!catsDir.isDirectory()) {
                Catalog a = Json.load(catsDir.getPath(), Catalog.class);
                catalogs.add(a);
            }
        }
        catalogs.sort(Comparator.comparing(Catalog::getId));
        users.clear();
        File[] usrsDir = Pipe.list("USRS");
        for (File file : usrsDir) {
            if (!file.isDirectory()) {
                User a = Json.load(file.getPath(), User.class);
                users.add(a);
            }
        }
        users.sort(Comparator.comparing(User::getId));
        orders.clear();
        File[] ordsDir = Pipe.list("ORDS");
        for (File file : ordsDir) {
            if (!file.isDirectory()) {
                PurchaseOrder a = Json.load(file.getPath(), PurchaseOrder.class);
                orders.add(a);
            }
        }
        orders.sort(Comparator.comparing(PurchaseOrder::getOrderId));
        ledgers.clear();
        File[] lgsDir = Pipe.list("LGS");
        for (File file : lgsDir) {
            if (!file.isDirectory()) {
                Ledger a = Json.load(file.getPath(), Ledger.class);
                ledgers.add(a);
            }
        }
        ledgers.sort(Comparator.comparing(Ledger::getId));
    }

    public static void setTheme(String themePath){
        configuration.setTheme(themePath);
    }

    public static void setDefaultModule(String modulePath) {
        configuration.setDefaultModule(modulePath);
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        Engine.configuration = configuration;
    }

    public static ArrayList<Organization> getOrganizations() {
        ArrayList<Organization> organizations = new ArrayList<>();
        File[] orgsDir = Pipe.list("ORGS");
        for (File file : orgsDir) {
            if (!file.isDirectory()) {
                Organization l = Json.load(file.getPath(), Organization.class);
                organizations.add(l);
            }
        }
        organizations.sort(Comparator.comparing(Organization::getId));
        return organizations;
    }

    public static ArrayList<Location> getCostCenters() {
        ArrayList<Location> costCenters = new ArrayList<>();
        File[] ccsDir = Pipe.list("CCS");
        for (File file : ccsDir) {
            if (!file.isDirectory()) {
                Location l = Json.load(file.getPath(), Location.class);
                costCenters.add(l);
            }
        }
        costCenters.sort(Comparator.comparing(Location::getId));
        return costCenters;
    }

    public static List<Location> getCostCenters(String id) {
        return getCostCenters().stream().filter(location -> location.getTie().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Location> getDistributionCenters() {
        return distributionCenters;
    }

    public static List<Location> getDistributionCenters(String id) {
        return distributionCenters.stream().filter(location -> location.getTie().equals(id)).collect(Collectors.toList());
    }

    public static List<Warehouse> getWarehouses(String id) {
        return warehouses.stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Warehouse> getWarehouses() {
        return warehouses;
    }

    public static ArrayList<Location> getCustomers() {
        return customers;
    }

    public static List<Location> getCustomers(String id) {
        return customers.stream().filter(location -> location.getTie().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Location> getVendors() {
        return vendors;
    }

    public static List<Location> getVendors(String id) {
        return vendors.stream().filter(location -> location.getTie().equals(id)).collect(Collectors.toList());
    }

    public static Location getVendor(String id){
        for(Location loc : vendors){
            if(loc.getId().equals(id)){
                return loc;
            }
        }
        return null;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static List<Item> getItems(String id) {
        return items.stream().filter(item -> item.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static Item getItem(String id) {
        for(Item item : items){
            if(item.getId().equals(id)){
                return item;
            }
        }
        return null;
    }

    public static ArrayList<Material> getMaterials() {
        return materials;
    }

    public static List<Material> getMaterials(String id) {
        return materials.stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Area> getAreas() {
        return areas;
    }

    public static List<Area> getAreas(String id) {
        return areas.stream().filter(location -> location.getValue("cost_center").equals(id)).collect(Collectors.toList());
    }

    public static void setOrganization(Organization organization) {
        Engine.organization = organization;
    }

    public static Organization getOrganization() {
        return organization;
    }

    public static Organization getOrganization(String orgId) {
        for(Organization o : getOrganizations()){
            if (o.getId().equals(orgId)) {
                return o;
            }
        }
        return null;
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static List<Employee> getEmployees(String id) {
        return employees.stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Catalog> getCatalogs() {
        return catalogs;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static ArrayList<PurchaseOrder> getOrders() {
        return orders;
    }

    public static List<PurchaseOrder> getOrders(String shipTo) {
        return orders.stream().filter(order -> order.getShipTo().equals(shipTo)).collect(Collectors.toList());
    }

    public static ArrayList<Ledger> getLedgers() {
        return ledgers;
    }

    public static List<Ledger> getLedgers(String id) {
        return ledgers.stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static JInternalFrame router(String transactionCode, DesktopState desktop){
        transactionCode = transactionCode.toUpperCase().trim();
        if(!transactionCode.startsWith("/")){
            transactionCode = "/" + transactionCode;
        }
        switch (transactionCode) {
            case "/ORGS", "/ORGS/F" -> {
                return new Organizations(desktop);
            }
            case "/ORGS/NEW" -> {
                return new CreateOrganization(desktop);
            }
            case "/ORGS/MOD" -> {
                return new CreateOrganization(desktop);
            }
            case "/CCS" -> {
                return new CostCenters(desktop);
            }
            case "/CCS/F" -> {
                return new FindCostCenter(desktop);
            }
            case "/CCS/NEW" -> {
                return new CreateCostCenter();
            }
            case "/CCS/MOD" -> {
                return new ModifyCostCenter(null);
            }
            case "/AREAS" -> {
                return new Areas(desktop);
            }
            case "/AREAS/NEW" -> {
                return new CreateArea(desktop, null);
            }
            case "/AREAS/MOD" -> {
                return new ModifyArea();
            }
            case "/CSTS" -> {
                return new Customers(desktop);
            }
            case "/CSTS/F" -> {
                return new FindCustomer(desktop);
            }
            case "/CSTS/NEW" -> {
                return new CreateCustomer(desktop);
            }
            case "/CSTS/MOD" -> {
                return new ModifyCustomer();
            }
            case "/DCSS" -> {
                return new DistributionCenters(desktop);
            }
            case "/DCSS/F" -> {
                return new FindDC(desktop);
            }
            case "/DCSS/NEW" -> {
                return new CreateDistributionCenter(desktop);
            }
            case "/DCSS/MOD" -> {
                return new ModifyDistributionCenter(null);
            }
            case "/WHS" -> {
                return new Warehouses(desktop);
            }
            case "/WHS/NEW" -> {
                return new CreateWarehouse(desktop);
            }
            case "/WHS/F" -> {
                return new FindWarehouse(desktop);
            }
            case "/WHS/MOD" -> {
                return new ModifyWarehouse(null);
            }
            case "/VEND" -> {
                return new Vendors("Vendors", "/VEND", desktop);
            }
            case "/VEND/F" -> {
                return new FindVendor(desktop);
            }
            case "/VEND/NEW" -> {
                return new CreateVendor(desktop);
            }
            case "/MTS" -> {
                return new Materials(desktop);
            }
            case "/MTS/NEW" -> {
                return new CreateMaterial();
            }
            case "/MTS/MOD" -> {
                return new ModifyMaterial();
            }
            case "/LGS" -> {
                return new Ledgers(desktop);
            }
            case "/EMPS" -> {
                return new Employees(desktop);
            }
            case "/EMPS/F" -> {
                return new FindEmployee(desktop);
            }
            case "/EMPS/NEW" -> {
                return new CreateEmployee(desktop);
            }
            case "/EMPS/MOD" -> {
                return new ModifyEmployee(null); //TODO
            }
            case "/DPTS" -> {
                return new Departments(desktop);
            }
            case "/DPTS/F" -> {
                return new FindDepartment(desktop);
            }
            case "/DPTS/NEW" -> {
                return new CreateDepartment(desktop);
            }
            case "/DPTS/MOD" -> {
                return new ModifyDepartment();
            }
            case "/USRS" -> {
                return new Users(desktop);
            }
            case "/USRS/F" -> {
                return new FindUser(desktop);
            }
            case "/USRS/NEW" -> {
                return new CreateUser();
            }
            case "/USRS/MOD" -> {
                return new ModifyUser(null);
            }
            case "/INV" -> {
                return new Inventory();
            }
            case "/INVS" -> {
                return new Inventory();
            }
            case "/INVS/NEW" -> {
                return new CreateInvoice(null);
            }
            case "/CATS" -> {
                return new Catalogs(desktop);
            }
            case "/CATS/NEW" -> {
                return new CreateCatalog(null);
            }
            case "/CATS/MOD" -> {
                return new ModifyCatalog();
            }
            case "/ITS" -> {
                return new Items(desktop);
            }
            case "/ITS/NEW" -> {
                return new CreateItem();
            }
            case "/ITS/F" -> {
                return new FindItem(desktop);
            }
            case "/ITS/MOD" -> {
                return new ModifyItem();
            }
            case "/ORDS", "/ORDS/PO" -> {
                return new PurchaseOrders(desktop);
            }
            case "/ORDS/F", "/ORDS/PO/F" -> {
                return new FindPurchaseOrder(desktop);
            }
            case "/ORDS/NEW", "/ORDS/PO/NEW" -> {
                return new CreatePurchaseOrder();
            }
            case "/ORDS/PR" -> {
                return new PurchaseRequisitions(desktop);
            }
            case "/ORDS/PR/NEW" -> {
                return new CreatePurchaseRequisition();
            }
            case "/ORDS/PR/F" -> {
                return new FindPurchaseReq(desktop);
            }
            case "/ORDS/PR/AUTO_MK" -> {
                return new AutoMakePurchaseRequisitions();
            }
            case "/ORDS/SO/NEW" -> {
                return new CreateSalesOrder();
            }
            case "/INV/MV/STO" -> {
                return new CreateSTO();
            }
            case "/INV/PI/ITS" -> {
                return new InventoryForItem();
            }
            case "/INV/PI/MTS" -> {
                return new InventoryForMaterial();
            }
            case "/SHPS/RCV" -> {
                return new ReceiveOrder("", desktop);
            }
            case "/FIN" -> {
                return new Finance(desktop);
            }
            case "/CNL" -> {
                return new CanalSettings();
            }
            case "/CNL/EXIT" -> System.exit(-1);
            default -> {
                String[] chs = transactionCode.split("/");
                String t = chs[1];
                String oid = chs[2];
                System.out.println(oid);
                switch (t) {
                    case "ORGS" -> new OrgView(Engine.getOrganization(oid), desktop);
                    case "AREAS" -> {
                        for(Area l : Engine.getAreas()){
                            if(l.getId().equals(oid)){

                            }
                        }
                    }
                    case "CCS" -> {
                        for(Location l : Engine.getCostCenters()){
                            if(l.getId().equals(oid)){
                                return new CostCenterView(l, desktop);
                            }
                        }
                    }
                    case "CSTS" -> {
                        for(Location l : Engine.getCustomers()){
                            if(l.getId().equals(oid)){
                                return new CustomerView(l);
                            }
                        }
                    }
                    case "DCSS" -> {
                        for(Location l : Engine.getDistributionCenters()){
                            if(l.getId().equals(oid)){
                                return new DCView(l, desktop);
                            }
                        }
                    }
                    case "VEND" -> {
                        for(Location l : Engine.getVendors()){
                            if(l.getId().equals(oid)){
                                return new VendorView(l);
                            }
                        }
                    }
                    case "ITS" -> {
                        for(Item l : Engine.getItems()){
                            if(l.getId().equals(oid)){
                                return new ItemView(l);
                            }
                        }
                    }
                    case "EMPS" -> {
                        for(Employee e : Engine.getEmployees()){
                            if(e.getId().equals(oid)){
                                return new EmployeeView(e);
                            }
                        }
                    }
                    case "MTS" -> {
                        return new Materials(desktop);
                    }
                    case "LGS" -> {
                        return new Ledgers(desktop);
                    }
                    case "USRS" -> {
                        return new Users(desktop);
                    }
                    case "INV" -> {
                        return new Inventory();
                    }
                    case "CATS" -> {
                        return new Catalogs(desktop);
                    }
                }
            }
        }
        return null;
    }

    public static void adjustColumnWidths(JTable table) {
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn column = table.getColumnModel().getColumn(col);
            int maxWidth;
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, col);
            maxWidth = headerComponent.getPreferredSize().width;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
                Component cellComponent = cellRenderer.getTableCellRendererComponent(table, table.getValueAt(row, col), false, false, row, col);
                int cellWidth = cellComponent.getPreferredSize().width;
                maxWidth = Math.max(maxWidth, cellWidth);
            }
            column.setPreferredWidth(maxWidth + 10);
        }
    }

    public static class realtime {
        public static ArrayList<PurchaseRequisition> getPurchaseRequisitions() {
            ArrayList<PurchaseRequisition> prs = new ArrayList<>();
            File[] posDir = Pipe.list("PR");
            for(int i = 0; i < posDir.length; i++){
                if(!posDir[i].isDirectory()){
                    PurchaseRequisition a = Json.load(posDir[i].getPath(), PurchaseRequisition.class);
                    prs.add(a);
                }
            }
            prs.sort(Comparator.comparing(PurchaseRequisition::getId));
            return prs;
        }
        public static PurchaseRequisition getPurchaseRequisitions(String prNumber) {
            File[] posDir = Pipe.list("PR");
            for(int i = 0; i < posDir.length; i++){
                if(!posDir[i].isDirectory()){
                    PurchaseRequisition a = Json.load(posDir[i].getPath(), PurchaseRequisition.class);
                    if(a.getName().equals(prNumber)){
                        return a;
                    }
                }
            }
            return null;
        }
        public static ArrayList<PurchaseOrder> getPurchaseOrders() {
            ArrayList<PurchaseOrder> pos = new ArrayList<>();
            File[] posDir = Pipe.list("ORDS");
            for(int i = 0; i < posDir.length; i++){
                if(!posDir[i].isDirectory()){
                    PurchaseOrder a = Json.load(posDir[i].getPath(), PurchaseOrder.class);
                    pos.add(a);
                }
            }
            pos.sort(Comparator.comparing(PurchaseOrder::getOrderId));
            return pos;
        }
        public static PurchaseOrder getPurchaseOrders(String poNumber) {
            File[] posDir = Pipe.list("ORDS");
            for(int i = 0; i < posDir.length; i++){
                if(!posDir[i].isDirectory()){
                    PurchaseOrder a = Json.load(posDir[i].getPath(), PurchaseOrder.class);
                    if(a.getOrderId().equals(poNumber)){
                        return a;
                    }
                }
            }
            return null;
        }
        public static Item getItem(String poNumber) {
            for(Item i : items){
                if(i.getId().equals(poNumber)){
                    return i;
                }
            }
            return null;
        }
    }
}