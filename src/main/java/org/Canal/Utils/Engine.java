package org.Canal.Utils;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Views.Areas.*;
import org.Canal.UI.Views.Bins.*;
import org.Canal.UI.Views.Components.ArchiveComponent;
import org.Canal.UI.Views.Components.Components;
import org.Canal.UI.Views.Components.CreateComponent;
import org.Canal.UI.Views.Components.FindComponent;
import org.Canal.UI.Views.Controllers.*;
import org.Canal.UI.Views.CreateLocation;
import org.Canal.UI.Views.Distribution.DistributionCenters.*;
import org.Canal.UI.Views.Distribution.Vendors.FindVendor;
import org.Canal.UI.Views.Distribution.Vendors.VendorView;
import org.Canal.UI.Views.Distribution.Warehouses.*;
import org.Canal.UI.Views.Finance.Catalogs.Catalogs;
import org.Canal.UI.Views.Finance.Catalogs.CreateCatalog;
import org.Canal.UI.Views.Finance.Catalogs.FindCatalog;
import org.Canal.UI.Views.Finance.Catalogs.ModifyCatalog;
import org.Canal.UI.Views.Finance.CostCenters.*;
import org.Canal.UI.Views.Finance.Customers.*;
import org.Canal.UI.Views.Finance.GoodsReceipts.FindGoodsReceipt;
import org.Canal.UI.Views.Finance.GoodsReceipts.GoodsReceipts;
import org.Canal.UI.Views.Finance.Ledgers.CreateLedger;
import org.Canal.UI.Views.Finance.Ledgers.LedgerView;
import org.Canal.UI.Views.Finance.Ledgers.Ledgers;
import org.Canal.UI.Views.HR.Departments.CreateDepartment;
import org.Canal.UI.Views.HR.Departments.Departments;
import org.Canal.UI.Views.HR.Departments.FindDepartment;
import org.Canal.UI.Views.HR.Departments.ModifyDepartment;
import org.Canal.UI.Views.HR.Employees.*;
import org.Canal.UI.Views.HR.Organizations.ModifyOrganization;
import org.Canal.UI.Views.HR.Organizations.OrgView;
import org.Canal.UI.Views.HR.Positions.CreatePosition;
import org.Canal.UI.Views.HR.Teams.CreateTeam;
import org.Canal.UI.Views.HR.Teams.ModifyTeam;
import org.Canal.UI.Views.HR.Teams.Teams;
import org.Canal.UI.Views.HR.Users.*;
import org.Canal.UI.Views.Inventory.*;
import org.Canal.UI.Views.Items.*;
import org.Canal.UI.Views.Locations;
import org.Canal.UI.Views.Materials.FindMaterial;
import org.Canal.UI.Views.Materials.Materials;
import org.Canal.UI.Views.Orders.*;
import org.Canal.UI.Views.Materials.CreateMaterial;
import org.Canal.UI.Views.Materials.ModifyMaterial;
import org.Canal.UI.Views.Invoices.CreateInvoice;
import org.Canal.UI.Views.Orders.PurchaseOrders.*;
import org.Canal.UI.Views.Orders.PurchaseRequisitions.AutoMakePurchaseRequisitions;
import org.Canal.UI.Views.Orders.PurchaseRequisitions.CreatePurchaseRequisition;
import org.Canal.UI.Views.Orders.PurchaseRequisitions.FindPurchaseReq;
import org.Canal.UI.Views.Orders.PurchaseRequisitions.PurchaseRequisitions;
import org.Canal.UI.Views.Orders.SalesOrders.AutoMakeSalesOrders;
import org.Canal.UI.Views.Orders.SalesOrders.CreateSalesOrder;
import org.Canal.UI.Views.Orders.SalesOrders.SalesOrders;
import org.Canal.UI.Views.Productivity.Notes.*;
import org.Canal.UI.Views.Productivity.Tasks.CreateTask;
import org.Canal.UI.Views.Productivity.Tasks.TaskList;
import org.Canal.UI.Views.Transportation.Carriers.*;
import org.Canal.UI.Views.Transportation.InboundDeliveryOrders.*;
import org.Canal.UI.Views.Transportation.OutboundDeliveryOrders.*;
import org.Canal.UI.Views.Transportation.Trucks.ArchiveTruck;
import org.Canal.UI.Views.Transportation.Trucks.FindTruck;
import org.Canal.UI.Views.Transportation.Trucks.RemoveTruck;
import org.Canal.UI.Views.Transportation.Trucks.Trucks;
import org.Canal.UI.Views.ValueAddedServices.CreateVAS;
import org.Canal.UI.Views.ValueAddedServices.ValueAddedServices;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for fetching and maintaining Canal objex.
 */
public class Engine {

    private static Configuration configuration;
    public static Organization organization;
    public static User assignedUser;

    public static User getAssignedUser() {
        return assignedUser;
    }

    public static void assignUser(User assignedUser) {
        Engine.assignedUser = assignedUser;
        configuration.setAssignedUser(assignedUser.getId());
        Pipe.saveConfiguration();
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

    public static ArrayList<Location> getLocations(String objex) {
        ArrayList<Location> distributionCenters = new ArrayList<>();
        File[] dcssDir = Pipe.list(objex);
        for (File file : dcssDir) {
            if (!file.isDirectory()) {
                Location l = Json.load(file.getPath(), Location.class);
                distributionCenters.add(l);
            }
        }
        distributionCenters.sort(Comparator.comparing(Location::getId));
        return distributionCenters;
    }

    public static List<Location> getLocations(String org, String objex) {
        return getLocations(objex).stream().filter(location -> location.getOrganization().equals(org)).collect(Collectors.toList());
    }

    public static Location getLocation(String id, String objex) {
        for(Location l : getLocations(objex)) {
            if(l.getId().equals(id)) {
                return l;
            }
        }
        return null;
    }

    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        File[] itsDir = Pipe.list("ITS");
        for (File file : itsDir) {
            if (!file.isDirectory()) {
                Item l = Json.load(file.getPath(), Item.class);
                items.add(l);
            }
        }
        items.sort(Comparator.comparing(Item::getId));
        return items;
    }

    public static List<Item> getItems(String id) {
        return getItems().stream().filter(item -> item.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static Item getItem(String id) {
        for(Item item : getItems()){
            if(item.getId().equals(id)){
                return item;
            }
        }
        return null;
    }

    public static ArrayList<Material> getMaterials() {
        ArrayList<Material> materials = new ArrayList<>();
        File[] mtsDir = Pipe.list("MTS");
        for (File file : mtsDir) {
            if (!file.isDirectory()) {
                Material a = Json.load(file.getPath(), Material.class);
                materials.add(a);
            }
        }
        materials.sort(Comparator.comparing(Material::getId));
        return materials;
    }

    public static List<Material> getMaterials(String id) {
        return getMaterials().stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Area> getAreas() {
        ArrayList<Area> areas = new ArrayList<>();
        File[] areasDir = Pipe.list("AREAS");
        for (File file : areasDir) {
            if (!file.isDirectory()) {
                Area a = Json.load(file.getPath(), Area.class);
                areas.add(a);
            }
        }
        areas.sort(Comparator.comparing(Area::getId));
        return areas;
    }

    public static List<Area> getAreas(String id) {
        return getAreas().stream().filter(area -> area.getLocation().equals(id)).collect(Collectors.toList());
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
        ArrayList<Employee> employees = new ArrayList<>();
        File[] empDir = Pipe.list("EMPS");
        for (File file : empDir) {
            if (!file.isDirectory()) {
                Employee a = Json.load(file.getPath(), Employee.class);
                employees.add(a);
            }
        }
        employees.sort(Comparator.comparing(Employee::getId));
        return employees;
    }

    public static List<Employee> getEmployees(String id) {
        return getEmployees().stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static Employee getEmployee(String Id){
        for(Employee e : getEmployees()){
            if(e.getId().equals(Id)){
                return e;
            }
        }
        return null;
    }

    public static ArrayList<Catalog> getCatalogs() {
        ArrayList<Catalog> catalogs = new ArrayList<>();
        File[] catsDirs = Pipe.list("CATS");
        for (File catsDir : catsDirs) {
            if (!catsDir.isDirectory()) {
                Catalog a = Json.load(catsDir.getPath(), Catalog.class);
                catalogs.add(a);
            }
        }
        catalogs.sort(Comparator.comparing(Catalog::getId));
        return catalogs;
    }

    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        File[] usrsDir = Pipe.list("USRS");
        for (File file : usrsDir) {
            if (!file.isDirectory()) {
                User a = Json.load(file.getPath(), User.class);
                users.add(a);
            }
        }
        users.sort(Comparator.comparing(User::getId));
        return users;
    }

    public static User getUser(String id) {
        for(User u : getUsers()){
            if(u.getId().equals(id)){
                return u;
            }
        }
        return null;
    }

    public static ArrayList<PurchaseOrder> getOrders() {
        ArrayList<PurchaseOrder> orders = new ArrayList<>();
        File[] ordsDir = Pipe.list("ORDS");
        for (File file : ordsDir) {
            if (!file.isDirectory()) {
                PurchaseOrder a = Json.load(file.getPath(), PurchaseOrder.class);
                orders.add(a);
            }
        }
        orders.sort(Comparator.comparing(PurchaseOrder::getOrderId));
        return orders;
    }

    public static List<PurchaseOrder> getOrders(String shipTo) {
        return getOrders().stream().filter(order -> order.getShipTo().equals(shipTo)).collect(Collectors.toList());
    }

    public static List<PurchaseOrder> getOrders(String shipTo, LockeStatus status) {
        return getOrders().stream().filter(order -> order.getShipTo().equals(shipTo) && order.getStatus().equals(status)).collect(Collectors.toList());
    }

    public static ArrayList<Ledger> getLedgers() {
        ArrayList<Ledger> ledgers = new ArrayList<>();
        File[] lgsDir = Pipe.list("LGS");
        for (File file : lgsDir) {
            if (!file.isDirectory()) {
                Ledger a = Json.load(file.getPath(), Ledger.class);
                ledgers.add(a);
            }
        }
        ledgers.sort(Comparator.comparing(Ledger::getId));
        return ledgers;
    }

    public static ArrayList<Inventory> getInventories() {
        ArrayList<Inventory> inventories = new ArrayList<>();
        File[] empDir = Pipe.list("STK");
        for (File file : empDir) {
            if (!file.isDirectory()) {
                Inventory a = Json.load(file.getPath(), Inventory.class);
                inventories.add(a);
            }
        }
        inventories.sort(Comparator.comparing(Inventory::getLocation));
        return inventories;
    }

    public static Inventory getInventory(String location){
        var i = getInventories().stream().filter(inventory -> inventory.getLocation().equals(location)).toList().stream().findFirst().orElse(null);
        if(i == null){
            i = new Inventory(location);
            Pipe.save("/STK", i);
        }
        return i;
    }

    public static List<Ledger> getLedgers(String id) {
        return getLedgers().stream().filter(location -> location.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static JInternalFrame router(String transactionCode, DesktopState desktop) {
        transactionCode = transactionCode.toUpperCase().trim();
        if (!transactionCode.startsWith("/")) {
            transactionCode = "/" + transactionCode;
        }
        switch (transactionCode) {
            case "/ORGS", "/ORGS/F" -> {
                return new Locations("/ORGS", desktop);
            }
            case "/ORGS/NEW" -> {
                return new CreateLocation("/ORGS", desktop);
            }
            case "/ORGS/MOD" -> {
                return new ModifyOrganization();
            }
            case "/CCS" -> {
                return new Locations("/CCS", desktop);
            }
            case "/CCS/F" -> {
                return new FindCostCenter(desktop);
            }
            case "/CCS/NEW" -> {
                return new CreateLocation("/CCS", desktop);
            }
            case "/CCS/MOD" -> {
                return new ModifyCostCenter(Engine.getLocations("CCS").getFirst());
            }
            case "/AREAS" -> {
                return new Areas();
            }
            case "/AREAS/F" -> {
                return new FindArea(desktop);
            }
            case "/AREAS/NEW" -> {
                return new CreateArea(null, null);
            }
            case "/AREAS/MOD" -> {
                return new ModifyArea();
            }
            case "/AREAS/AUTO_MK" -> {
                return new AutoMakeAreas();
            }
            case "/BNS" -> {
                return new Bins();
            }
            case "/BNS/F" -> {
                return new FindBin(desktop);
            }
            case "/BNS/NEW" -> {
                return new CreateBin("", null);
            }
            case "/BNS/DEL" -> {
                return new RemoveBin();
            }
            case "/BNS/AUTO_MK" -> {
                return new AutoMakeBins();
            }
            case "/CSTS" -> {
                return new Locations("/CSTS", desktop);
            }
            case "/CSTS/F" -> {
                return new FindCustomer(desktop);
            }
            case "/CSTS/NEW" -> {
                return new CreateLocation("/CSTS", desktop);
            }
            case "/CSTS/MOD" -> {
                return new ModifyCustomer();
            }
            case "/DCSS" -> {
                return new Locations("/DCSS", desktop);
            }
            case "/DCSS/F" -> {
                return new FindDistributionCenter(desktop);
            }
            case "/DCSS/NEW" -> {
                return new CreateLocation("/DCSS", desktop);
            }
            case "/DCSS/MOD" -> {
                return new ModifyDistributionCenter(null);
            }
            case "/TRANS/ODO" -> {
                return new OutboundDeliveries(desktop);
            }
            case "/TRANS/ODO/NEW" -> {
                return new CreateOutboundDeliveryOrder();
            }
            case "/TRANS/ODO/F" -> {
                return new FindOutboundDeliveryOrder(desktop);
            }
            case "/TRANS/ODO/ARCHV" -> {
                return new ArchiveOutboundDeliveryOrder();
            }
            case "/TRANS/ODO/DEL" -> {
                return new RemoveOutboundDeliveryOrder();
            }
            case "/TRANS/IDO" -> {
                return new InboundDeliveries(desktop);
            }
            case "/TRANS/IDO/NEW" -> {
                return new CreateInboundDeliveryOrder();
            }
            case "/TRANS/IDO/F" -> {
                return new FindInboundDeliveryOrder(desktop);
            }
            case "/TRANS/IDO/ARCHV" -> {
                return new ArchiveInboundDeliveryOrder();
            }
            case "/TRANS/IDO/DEL" -> {
                return new RemoveInboundDeliveryOrder();
            }
            case "/TRANS/CRRS" -> {
                return new Carriers();
            }
            case "/TRANS/CRRS/F" -> {
                return new FindCarrier(desktop);
            }
            case "/TRANS/CRRS/NEW" -> {
                return new CreateCarrier(desktop);
            }
            case "/TRANS/CRRS/ARCHV" -> {
                return new ArchiveCarrier();
            }
            case "/TRANS/CRRS/DEL" -> {
                return new RemoveCarrier();
            }
            case "/TRANS/TRCKS" -> {
                return new Trucks();
            }
            case "/TRANS/TRCKS/F" -> {
                return new FindTruck(desktop);
            }
            case "/TRANS/TRCKS/NEW" -> {
                return new RemoveCarrier();
            }
            case "/TRANS/TRCKS/ARCHV" -> {
                return new ArchiveTruck();
            }
            case "/TRANS/TRCKS/DEL" -> {
                return new RemoveTruck();
            }
            case "/WHS" -> {
                return new Locations("/WHS", desktop);
            }
            case "/WHS/NEW" -> {
                return new CreateLocation("/WHS", desktop);
            }
            case "/WHS/F" -> {
                return new FindWarehouse(desktop);
            }
            case "/WHS/MOD" -> {
                return new ModifyWarehouse(null);
            }
            case "/VEND" -> {
                return new Locations("/VEND", desktop);
            }
            case "/VEND/F" -> {
                return new FindVendor(desktop);
            }
            case "/VEND/NEW" -> {
                return new CreateLocation("/VEND", desktop);
            }
            case "/MTS" -> {
                return new Materials(desktop);
            }
            case "/MTS/F" -> {
                return new FindMaterial(desktop);
            }
            case "/MTS/NEW" -> {
                return new CreateMaterial();
            }
            case "/MTS/MOD" -> {
                return new ModifyMaterial();
            }
            case "/CMPS" -> {
                return new Components(desktop);
            }
            case "/CMPS/F" -> {
                return new FindComponent(desktop);
            }
            case "/CMPS/NEW" -> {
                return new CreateComponent();
            }
            case "/CMPS/ARCHV" -> {
                return new ArchiveComponent();
            }
            case "/VAS" -> {
                return new ValueAddedServices();
            }
            case "/VAS/NEW" -> {
                return new CreateVAS();
            }
            case "/LGS" -> {
                return new Ledgers(desktop);
            }
            case "/LGS/NEW" -> {
                return new CreateLedger(desktop);
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
            case "/TMS" -> {
                return new Teams();
            }
            case "/TMS/NEW" -> {
                return new CreateTeam();
            }
            case "/TMS/MOD" -> {
                return new ModifyTeam();
            }
            case "/USRS" -> {
                return new Users(desktop);
            }
            case "/USRS/CHG_PSSWD" -> {
                return new ChangeUserPassword();
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
            case "/CNL/INV" -> {
                return new org.Canal.UI.Views.Controllers.Inventory();
            }
            case "/STK" -> {
                return new InventoryView(desktop, Engine.getOrganization().getId());
            }
            case "/STK/MOD/MV" -> {
                return new MoveStock("", null);
            }
            case "/INVS", "/INVS/NEW" -> {
                return new CreateInvoice(null);
            }
            case "/CATS" -> {
                return new Catalogs(desktop);
            }
            case "/CATS/NEW" -> {
                return new CreateCatalog(null);
            }
            case "/CATS/MOD" -> {
                return new ModifyCatalog(null);
            }
            case "/CATS/F" -> {
                return new FindCatalog(desktop);
            }
            case "/ITS" -> {
                return new Items(desktop);
            }
            case "/ITS/NEW" -> {
                return new CreateItem(desktop);
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
            case "/ORDS/RCV" -> {
                return new ReceiveOrder(Engine.getOrganization().getId(), desktop);
            }
            case "/ORDS/F", "/ORDS/PO/F" -> {
                return new FindPurchaseOrder(desktop);
            }
            case "/ORDS/NEW", "/ORDS/PO/NEW" -> {
                return new CreatePurchaseOrder();
            }
            case "/ORDS/RTRN" -> {
                return new ReturnOrder(desktop);
            }
            case "/ORDS/PO/AUTO_MK" -> {
                return new AutoMakePurchaseOrders();
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
            case "/ORDS/SO" -> {
                return new SalesOrders(desktop);
            }
            case "/ORDS/SO/NEW" -> {
                return new CreateSalesOrder();
            }
            case "/ORDS/SO/AUTO_MK" -> {
                return new AutoMakeSalesOrders();
            }
            case "/GR" -> {
                return new GoodsReceipts(desktop);
            }
            case "/GR/F" -> {
                return new FindGoodsReceipt(desktop);
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
            case "/NTS" -> {
                return new Notes();
            }
            case "/NTS/NEW" -> {
                return new CreateNote();
            }
            case "/NTS/MOD" -> {
                return new ModifyNote();
            }
            case "/NTS/ARCHV" -> {
                return new ArchiveNote();
            }
            case "/NTS/DEL" -> {
                return new RemoveNote();
            }
            case "/FIN" -> {
                return new Finance(desktop);
            }
            case "/MVMT/TSKS" -> {
                return new TaskList(null, desktop);
            }
            case "/MVMT/TSKS/NEW" -> {
                return new CreateTask();
            }
            case "/HR/POS/NEW" -> {
                return new CreatePosition(desktop);
            }
            case "/CNL/DATA_CNTR" -> {
                return new DataCenter();
            }
            case "/CNL/HR" -> {
                return new HumanResources(desktop);
            }
            case "/TM_CLCK" -> {
                return new TimeClock(desktop);
            }
            case "/CNL" -> {
                return new CanalSettings();
            }
            case "/LOGIN" -> {
                return new Login(true);
            }
            case "/CLEAR_DSK" -> desktop.clean();
            case "/CLOSE_DSK" -> desktop.purge();
            case "/CNL/EXIT" -> System.exit(-1);
            default -> {
                String[] chs = transactionCode.split("/");
                String t = chs[1];
                String oid = chs[2];
                switch (t) {
                    case "ORGS" -> {
                        for (Location org : Engine.getLocations("ORGS")) {
                            new OrgView(org, desktop);
                        }
                    }
                    case "AREAS" -> {
                        for(Area l : Engine.getAreas()){
                            if(l.getId().equals(oid)){

                            }
                        }
                    }
                    case "CCS" -> {
                        for(Location l : Engine.getLocations("CCS")){
                            if(l.getId().equals(oid)){
                                return new CostCenterView(l, desktop);
                            }
                        }
                    }
                    case "CSTS" -> {
                        for(Location l : Engine.getLocations("CSTS")){
                            if(l.getId().equals(oid)){
                                return new CustomerView(l);
                            }
                        }
                    }
                    case "DCSS" -> {
                        for(Location l : Engine.getLocations("DCSS")){
                            if(l.getId().equals(oid)){
                                return new DCView(l, desktop);
                            }
                        }
                    }
                    case "VEND" -> {
                        for(Location l : Engine.getLocations("VEND")){
                            if(l.getId().equals(oid)){
                                return new VendorView(l);
                            }
                        }
                    }
                    case "ITS" -> {
                        Item i = Engine.getItem(oid);
                        if(i != null){
                            return new ItemView(i);
                        }
                    }
                    case "USRS" -> {
                        User u = Engine.getUser(oid);
                        if(u != null){
                            return new UserView(desktop, u);
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
                        for(Ledger l : getLedgers()){
                            if(l.getId().equals(oid)){
                                return new LedgerView(l, desktop);
                            }
                        }
                        return new Ledgers(desktop);
                    }
                    case "WHS" -> {
                        for(Location l : getLocations("WHS")){
                            if(l.getId().equals(oid)){
                                return new WarehouseView(l, desktop);
                            }
                        }
                        return new Locations("/WHS", desktop);
                    }
                    case "ORDS" -> {
                        for(PurchaseOrder l : Engine.orderProcessing.getPurchaseOrder()){
                            if(l.getOrderId().equals(oid)){
                                return new PurchaseOrderView(l);
                            }
                        }
                        return new Locations("/ORDS", desktop);
                    }
                    case "INV" -> {
                        return new org.Canal.UI.Views.Controllers.Inventory();
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

    public static class orderProcessing {

        public static PurchaseRequisition getPurchaseRequisitions(String prNumber) {
            File[] posDir = Pipe.list("PR");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    PurchaseRequisition a = Json.load(file.getPath(), PurchaseRequisition.class);
                    if (a.getName().equals(prNumber)) {
                        return a;
                    }
                }
            }
            return null;
        }

        public static ArrayList<PurchaseOrder> getPurchaseOrder() {
            ArrayList<PurchaseOrder> pos = new ArrayList<>();
            File[] posDir = Pipe.list("ORDS");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    PurchaseOrder a = Json.load(file.getPath(), PurchaseOrder.class);
                    pos.add(a);
                }
            }
            pos.sort(Comparator.comparing(PurchaseOrder::getOrderId));
            return pos;
        }

        public static PurchaseOrder getPurchaseOrder(String poNumber) {
            File[] posDir = Pipe.list("ORDS");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    PurchaseOrder a = Json.load(file.getPath(), PurchaseOrder.class);
                    if (a.getOrderId().equals(poNumber)) {
                        return a;
                    }
                }
            }
            return null;
        }

        public static ArrayList<SalesOrder> getSalesOrders() {
            ArrayList<SalesOrder> sos = new ArrayList<>();
            File[] posDir = Pipe.list("ORDS/SO");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    SalesOrder a = Json.load(file.getPath(), SalesOrder.class);
                    sos.add(a);
                }
            }
            sos.sort(Comparator.comparing(SalesOrder::getOrderId));
            return sos;
        }

        public static SalesOrder getSalesOrder(String poNumber) {
            File[] posDir = Pipe.list("ORDS/SO");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    SalesOrder a = Json.load(file.getPath(), SalesOrder.class);
                    if (a.getOrderId().equals(poNumber)) {
                        return a;
                    }
                }
            }
            return null;
        }

        public static ArrayList<PurchaseRequisition> getPurchaseRequisitions() {
            ArrayList<PurchaseRequisition> prs = new ArrayList<>();
            File[] posDir = Pipe.list("PR");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    PurchaseRequisition a = Json.load(file.getPath(), PurchaseRequisition.class);
                    prs.add(a);
                }
            }
            prs.sort(Comparator.comparing(PurchaseRequisition::getId));
            return prs;
        }

        public static ArrayList<GoodsReceipt> getGoodsReceipts() {
            ArrayList<GoodsReceipt> pos = new ArrayList<>();
            File[] posDir = Pipe.list("GR");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    GoodsReceipt a = Json.load(file.getPath(), GoodsReceipt.class);
                    pos.add(a);
                }
            }
            pos.sort(Comparator.comparing(GoodsReceipt::getId));
            return pos;
        }
    }

    public static Area getArea(String areaId) {
        File[] areaDir = Pipe.list("AREAS");
        for (File file : areaDir) {
            if (!file.isDirectory()) {
                Area a = Json.load(file.getPath(), Area.class);
                if (a.getId().equals(areaId)) {
                    return a;
                }
            }
        }
        return null;
    }
}