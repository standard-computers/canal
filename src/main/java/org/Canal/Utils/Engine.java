package org.Canal.Utils;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.Models.HumanResources.Timesheet;
import org.Canal.Models.HumanResources.User;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.Models.Record;
import org.Canal.Start;
import org.Canal.UI.Views.*;
import org.Canal.UI.Views.Areas.*;
import org.Canal.UI.Views.Bins.*;
import org.Canal.UI.Views.Customers.ViewCustomer;
import org.Canal.UI.Views.Departments.DeleteDepartment;
import org.Canal.UI.Views.Finance.Accounts.Accounts;
import org.Canal.UI.Views.Finance.Accounts.CreateAccount;
import org.Canal.UI.Views.Finance.PurchaseOrders.*;
import org.Canal.UI.Views.Positions.Positions;
import org.Canal.UI.Views.Productivity.Flows.CreateFlow;
import org.Canal.UI.Views.Productivity.Waves.CreateWave;
import org.Canal.UI.Views.Productivity.WorkOrders.CreateWorkOrder;
import org.Canal.UI.Views.Products.Components.Components;
import org.Canal.UI.Views.Products.Components.CreateComponent;
import org.Canal.UI.Views.Controllers.*;
import org.Canal.UI.Views.Employees.CreateEmployee;
import org.Canal.UI.Views.Employees.ViewEmployee;
import org.Canal.UI.Views.Employees.Employees;
import org.Canal.UI.Views.Finance.Catalogs.Catalogs;
import org.Canal.UI.Views.Finance.Catalogs.CreateCatalog;
import org.Canal.UI.Views.Finance.GoodsReceipts.GoodsReceipts;
import org.Canal.UI.Views.Finance.Ledgers.CreateLedger;
import org.Canal.UI.Views.Finance.Ledgers.ViewLedger;
import org.Canal.UI.Views.Finance.Ledgers.Ledgers;
import org.Canal.UI.Views.Departments.CreateDepartment;
import org.Canal.UI.Views.Departments.Departments;
import org.Canal.UI.Views.Distribution.InboundDeliveryOrders.CreateInboundDeliveryOrder;
import org.Canal.UI.Views.Distribution.InboundDeliveryOrders.InboundDeliveries;
import org.Canal.UI.Views.Notes.CreateNote;
import org.Canal.UI.Views.Notes.Notes;
import org.Canal.UI.Views.Distribution.OutboundDeliveryOrders.CreateOutboundDeliveryOrder;
import org.Canal.UI.Views.Distribution.OutboundDeliveryOrders.OutboundDeliveries;
import org.Canal.UI.Views.Positions.CreatePosition;
import org.Canal.UI.Views.Products.Items.CreateItem;
import org.Canal.UI.Views.Products.Items.ViewItem;
import org.Canal.UI.Views.Products.Items.Items;
import org.Canal.UI.Views.System.CanalSettings;
import org.Canal.UI.Views.System.QuickExplorer;
import org.Canal.UI.Views.Teams.CreateTeam;
import org.Canal.UI.Views.Teams.Teams;
import org.Canal.UI.Views.Inventory.*;
import org.Canal.UI.Views.Products.Materials.Materials;
import org.Canal.UI.Views.Products.Materials.CreateMaterial;
import org.Canal.UI.Views.Finance.Invoices.CreateInvoice;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.AutoMakePurchaseRequisitions;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.CreatePurchaseRequisition;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.PurchaseRequisitions;
import org.Canal.UI.Views.Finance.SalesOrders.AutoMakeSalesOrders;
import org.Canal.UI.Views.Finance.SalesOrders.CreateSalesOrder;
import org.Canal.UI.Views.Finance.SalesOrders.SalesOrders;
import org.Canal.UI.Views.Productivity.Tasks.CreateTask;
import org.Canal.UI.Views.Productivity.Tasks.TaskList;
import org.Canal.UI.Views.Distribution.Trucks.CreateTruck;
import org.Canal.UI.Views.Distribution.Trucks.Trucks;
import org.Canal.UI.Views.Users.*;
import org.Canal.UI.Views.ValueAddedServices.CreateVAS;
import org.Canal.UI.Views.ValueAddedServices.ValueAddedServices;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for fetching and maintaining Canal objex.
 */
public class Engine {

    private static Configuration configuration = new Configuration();
    public static Codex codex;
    public static Location organization;
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

    public static void setOrganization(Location organization) {
        Engine.organization = organization;
    }

    public static Location getOrganization() {
        return organization;
    }

    public static ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        String[] locs = new String[]{"DCSS", "CCS", "CSTS", "ORGS", "VEND", "WHS", "TRANS/CRRS" };
        for(String l : locs) {
            File[] d = Pipe.list(l);
            for (File file : d) {
                if (!file.isDirectory() && file.getPath().endsWith("." + l.toLowerCase())) {
                    Location loc = Json.load(file.getPath(), Location.class);
                    locations.add(loc);
                }
            }
        }
        locations.sort(Comparator.comparing(Location::getId));
        return locations;
    }

    public static ArrayList<Location> getLocations(String objex) {
        ArrayList<Location> locations = new ArrayList<>();
        File[] d = Pipe.list(objex);
        for (File file : d) {
            if (!file.isDirectory()) {
                Location l = Json.load(file.getPath(), Location.class);
                locations.add(l);
            }
        }
        locations.sort(Comparator.comparing(Location::getId));
        return locations;
    }

    public static List<Location> getLocations(String org, String objex) {
        return getLocations(objex).stream().filter(l -> l.getOrganization().equals(org)).collect(Collectors.toList());
    }

    public static Location getLocation(String id, String objex) {
        for(Location l : getLocations(objex)) {
            if(l.getId().equals(id)) {
                return l;
            }
        }
        return null;
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
        return getAreas().stream().filter(a -> a.getLocation().equals(id)).collect(Collectors.toList());
    }

    public static Area getArea(String id) {
        return getAreas().stream().filter(c -> c.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        File[] d = Pipe.list("EMPS");
        for (File file : d) {
            if (!file.isDirectory()) {
                Employee a = Json.load(file.getPath(), Employee.class);
                employees.add(a);
            }
        }
        employees.sort(Comparator.comparing(Employee::getId));
        return employees;
    }

    public static List<Employee> getEmployees(String id) {
        return getEmployees().stream().filter(e -> e.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static Employee getEmployee(String id){
        return getEmployees().stream().filter(e -> e.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Timesheet> getTimesheets() {
        ArrayList<Timesheet> timesheets = new ArrayList<>();
        File[] d = Pipe.list("HR/TMSH");
        for (File file : d) {
            if (!file.isDirectory()) {
                Timesheet a = Json.load(file.getPath(), Timesheet.class);
                timesheets.add(a);
            }
        }
        timesheets.sort(Comparator.comparing(Timesheet::getId));
        return timesheets;
    }

    public static Timesheet getTimesheet(String employeeId){
        var i = getTimesheets().stream().filter(e -> e.getEmployee().equals(employeeId)).toList().stream().findFirst().orElse(null);
        if(i == null){
            i = new Timesheet(employeeId);
            Pipe.save("/HR/TMSH", i);
        }
        return i;
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

    public static Catalog getCatalog(String id) {
        return getCatalogs().stream().filter(c -> c.getId().equals(id)).toList().stream().findFirst().orElse(null);
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
        return getUsers().stream().filter(u -> u.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<PurchaseOrder> getPurchaseOrders() {
        ArrayList<PurchaseOrder> orders = new ArrayList<>();
        File[] ordsDir = Pipe.list("ORDS/PO");
        for (File file : ordsDir) {
            if (!file.isDirectory()) {
                PurchaseOrder a = Json.load(file.getPath(), PurchaseOrder.class);
                orders.add(a);
            }
        }
        orders.sort(Comparator.comparing(PurchaseOrder::getOrderId));
        return orders;
    }

    public static List<PurchaseOrder> getPurchaseOrders(String shipTo) {
        return getPurchaseOrders().stream().filter(order -> order.getShipTo().equals(shipTo)).collect(Collectors.toList());
    }

    public static List<PurchaseOrder> getPurchaseOrders(String shipTo, LockeStatus status) {
        return getPurchaseOrders().stream().filter(order -> order.getShipTo().equals(shipTo) && order.getStatus().equals(status)).collect(Collectors.toList());
    }

    public static ArrayList<Ledger> getLedgers() {
        ArrayList<Ledger> ledgers = new ArrayList<>();
        File[] d = Pipe.list("LGS");
        for (File file : d) {
            if (!file.isDirectory()) {
                Ledger a = Json.load(file.getPath(), Ledger.class);
                ledgers.add(a);
            }
        }
        ledgers.sort(Comparator.comparing(Ledger::getId));
        return ledgers;
    }

    public static Ledger getLedger(String id){
        return getLedgers().stream().filter(inventory -> inventory.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Truck> getTrucks() {
        ArrayList<Truck> trucks = new ArrayList<>();
        File[] d = Pipe.list("TRANS/TRCKS");
        for (File file : d) {
            if (!file.isDirectory()) {
                Truck a = Json.load(file.getPath(), Truck.class);
                trucks.add(a);
            }
        }
        trucks.sort(Comparator.comparing(Truck::getId));
        return trucks;
    }

    public static ArrayList<Position> getPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        File[] d = Pipe.list("HR/POS");
        for (File file : d) {
            if (!file.isDirectory()) {
                Position a = Json.load(file.getPath(), Position.class);
                positions.add(a);
            }
        }
        positions.sort(Comparator.comparing(Position::getId));
        return positions;
    }

    public static Position getPosition(String id){
        return getPositions().stream().filter(position -> position.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Delivery> getInboundDeliveries() {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        File[] d = Pipe.list("TRANS/IDO");
        for (File file : d) {
            if (!file.isDirectory()) {
                Delivery a = Json.load(file.getPath(), Delivery.class);
                deliveries.add(a);
            }
        }
        deliveries.sort(Comparator.comparing(Delivery::getId));
        return deliveries;
    }

    public static ArrayList<Delivery> getInboundDeliveries(String destination) {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        File[] d = Pipe.list("TRANS/IDO");
        for (File file : d) {
            if (!file.isDirectory()) {
                Delivery a = Json.load(file.getPath(), Delivery.class);
                if(a.getDestination().equals(destination)){
                    deliveries.add(a);
                }
            }
        }
        deliveries.sort(Comparator.comparing(Delivery::getId));
        return deliveries;
    }

    public static Delivery getInboundDelivery(String id){
        return getInboundDeliveries().stream().filter(delivery -> delivery.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Delivery> getOutboundDeliveries() {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        File[] d = Pipe.list("TRANS/ODO");
        for (File file : d) {
            if (!file.isDirectory()) {
                Delivery a = Json.load(file.getPath(), Delivery.class);
                deliveries.add(a);
            }
        }
        deliveries.sort(Comparator.comparing(Delivery::getId));
        return deliveries;
    }

    public static ArrayList<Delivery> getOutboundDeliveries(String destination) {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        File[] d = Pipe.list("TRANS/ODO");
        for (File file : d) {
            if (!file.isDirectory()) {
                Delivery a = Json.load(file.getPath(), Delivery.class);
                if(a.getOrigin().equals(destination)){
                    deliveries.add(a);
                }
            }
        }
        deliveries.sort(Comparator.comparing(Delivery::getId));
        return deliveries;
    }

    public static Delivery getOutboundDelivery(String id){
        return getOutboundDeliveries().stream().filter(delivery -> delivery.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Inventory> getInventories() {
        ArrayList<Inventory> is = new ArrayList<>();
        File[] d = Pipe.list("STK");
        for (File f : d) {
            if (!f.isDirectory()) {
                Inventory a = Json.load(f.getPath(), Inventory.class);
                is.add(a);
            }
        }
        is.sort(Comparator.comparing(Inventory::getLocation));
        return is;
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
        return getLedgers().stream().filter(location -> location.getOrganization().equals(id)).collect(Collectors.toList());
    }

    public static ArrayList<Record> getRecords(String objex, String id) {
        File[] d = Pipe.list("RCS");
        for (File f : d) {
            if (!f.isDirectory()) {
                if(f.getPath().endsWith(id + "." + objex.toLowerCase().replaceAll("/", "."))){
                    return Json.load(f.getPath(), ArrayList.class);
                }
            }
        }
        return null;
    }

    public static ArrayList<ArrayList<Record>> getRecords() {
        ArrayList<ArrayList<Record>> all = new ArrayList<>();
        File[] d = Pipe.list("RCS");
        for (File f : d) {
            if (!f.isDirectory()) {
                all.add(Json.load(f.getPath(), ArrayList.class));
            }
        }
        return all;
    }

    public static Object codex(String objex, String key){
        return (Engine.codex.getValue(objex, key) == null ? false : Engine.codex.getValue(objex, key));
    }

    public static JInternalFrame router(String transactionCode, DesktopState desktop) {
        transactionCode = transactionCode.toUpperCase().trim();
        if (!transactionCode.startsWith("/")) {
            transactionCode = "/" + transactionCode;
        }
        switch (transactionCode) {
            case "/ORGS" -> {
                return new Locations("/ORGS", desktop);
            }
            case "/ORGS/NEW" -> {
                return new CreateLocation("/ORGS", desktop, null);
            }
            case "/CCS" -> {
                return new Locations("/CCS", desktop);
            }
            case "/CCS/NEW" -> {
                return new CreateLocation("/CCS", desktop, null);
            }
            case "/AREAS" -> {
                return new Areas();
            }
            case "/AREAS/NEW" -> {
                return new CreateArea(null, null);
            }
            case "/AREAS/AUTO_MK" -> {
                return new AutoMakeAreas();
            }
            case "/BNS" -> {
                return new Bins(desktop);
            }
            case "/BNS/F" -> {
                return new Finder("/BNS", desktop);
            }
            case "/BNS/NEW" -> {
                return new CreateBin("", null);
            }
            case "/BNS/AUTO_MK" -> {
                return new AutoMakeBins();
            }
            case "/BNS/DEL" -> {
                return new RemoveBin();
            }
            case "/CSTS" -> {
                return new Locations("/CSTS", desktop);
            }
            case "/CSTS/NEW" -> {
                return new CreateLocation("/CSTS", desktop, null);
            }
            case "/ACCS" -> {
                return new Accounts(desktop);
            }
            case "/ACCS/NEW" -> {
                return new CreateAccount();
            }
            case "/DCSS" -> {
                return new Locations("/DCSS", desktop);
            }
            case "/DCSS/NEW" -> {
                return new CreateLocation("/DCSS", desktop, null);
            }
            case "/TRANS/ODO" -> {
                return new OutboundDeliveries(desktop);
            }
            case "/TRANS/ODO/NEW" -> {
                return new CreateOutboundDeliveryOrder();
            }
            case "/TRANS/IDO" -> {
                return new InboundDeliveries(desktop);
            }
            case "/TRANS/IDO/NEW" -> {
                return new CreateInboundDeliveryOrder();
            }
            case "/TRANS/CRRS" -> {
                return new Locations("/TRANS/CRRS", desktop);
            }
            case "/TRANS/CRRS/NEW" -> {
                return new CreateLocation("/TRANS/CRRS", desktop, null);
            }
            case "/TRANS/TRCKS" -> {
                return new Trucks(desktop);
            }
            case "/TRANS/TRCKS/NEW" -> {
                return new CreateTruck(desktop);
            }
            case "/WHS" -> {
                return new Locations("/WHS", desktop);
            }
            case "/WHS/NEW" -> {
                return new CreateLocation("/WHS", desktop, null);
            }
            case "/VEND" -> {
                return new Locations("/VEND", desktop);
            }
            case "/VEND/NEW" -> {
                return new CreateLocation("/VEND", desktop, null);
            }
            case "/MTS" -> {
                return new Materials(desktop);
            }
            case "/MTS/NEW" -> {
                return new CreateMaterial(desktop);
            }
            case "/CMPS" -> {
                return new Components(desktop);
            }
            case "/CMPS/NEW" -> {
                return new CreateComponent(desktop);
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
            case "/EMPS/NEW" -> {
                return new CreateEmployee(desktop, false);
            }
            case "/DPTS" -> {
                return new Departments(desktop);
            }
            case "/DPTS/NEW" -> {
                return new CreateDepartment(desktop);
            }
            case "/DPTS/DEL" -> {
                return new DeleteDepartment();
            }
            case "/TMS" -> {
                return new Teams();
            }
            case "/TMS/NEW" -> {
                return new CreateTeam();
            }
            case "/USRS" -> {
                return new Users(desktop);
            }
            case "/USRS/CHG_PSSWD" -> {
                return new ChangeUserPassword();
            }
            case "/USRS/NEW" -> {
                return new CreateUser();
            }
            case "/STK", "/INV" -> {
                return new ViewInventory(desktop, Engine.getOrganization().getId());
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
            case "/ITS" -> {
                return new Items(desktop);
            }
            case "/ITS/NEW" -> {
                return new CreateItem(desktop);
            }
            case "/ORDS/PO" -> {
                return new PurchaseOrders(desktop);
            }
            case "/ORDS/PO/NEW" -> {
                return new CreatePurchaseOrder(desktop);
            }
            case "/ORDS/PO/AUTO_MK" -> {
                return new AutoMakePurchaseOrders();
            }
            case "/ORDS/RCV" -> {
                return new ReceiveOrder(Engine.getOrganization().getId(), desktop);
            }
            case "/ORDS/RTRN" -> {
                return new ReturnOrder(desktop);
            }
            case "/ORDS/PR" -> {
                return new PurchaseRequisitions(desktop);
            }
            case "/ORDS/PR/NEW" -> {
                return new CreatePurchaseRequisition();
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
            case "/MVMT/TSKS" -> {
                return new TaskList(null, desktop);
            }
            case "/MVMT/FLWS/NEW" -> {
                return new CreateFlow();
            }
            case "/MVMT/WVS/NEW" -> {
                return new CreateWave();
            }
            case "/MVMT/WO/NEW" -> {
                return new CreateWorkOrder(desktop);
            }
            case "/MVMT/TSKS/NEW" -> {
                return new CreateTask();
            }
            case "/HR/POS" -> {
                return new Positions(desktop);
            }
            case "/HR/POS/NEW" -> {
                return new CreatePosition(desktop);
            }
            case "/DATA_CNTR" -> {
                return new DataCenter();
            }
            case "/HR" -> {
                return new HumanResources(desktop);
            }
            case "/FI" -> {
                return new Finance(desktop);
            }
            case "/ME" -> {
                return new MyProfile(desktop);
            }
            case "/PROD_MTN" -> {
                return new ProductMaintainence(desktop);
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
            case "/N" -> new QuickExplorer();
            case "/EXIT" -> System.exit(-1);
            case "/RSTRT" -> {

                String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                String vmArguments = String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments());
                String currentJar = System.getProperty("java.class.path");
                String command = javaBin + " " + vmArguments + " -cp " + currentJar + " " + Start.class.getName();
                ProcessBuilder builder = new ProcessBuilder(command.split(" "));
                builder.inheritIO();
                try {
                    builder.start();
                    System.exit(0);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to restart Canal");
                    throw new RuntimeException(e);
                }
            }
        }
        if(transactionCode.endsWith("/F")){
            return new Finder(transactionCode.replace("/F", ""), desktop);
        }else if(transactionCode.endsWith("/MOD")){
            return new Modifier(transactionCode.replace("/MOD", ""), null, null);
        }else if(transactionCode.endsWith("/ARCHV")){
            return new Archiver(transactionCode.replace("/ARCHV", ""));
        }else if(transactionCode.endsWith("/DEL")){
            return new Deleter(transactionCode.replace("/DEL", ""), null);
        }

        String[] chs = transactionCode.split("/");
        String t = chs[1];
        String oid = chs[2];
        switch (t) {
            case "ORGS" -> {
                for (Location org : Engine.getLocations("ORGS")) {
                    new ViewLocation(org, desktop);
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
                        return new ViewLocation(l, desktop);
                    }
                }
            }
            case "CSTS" -> {
                for(Location l : Engine.getLocations("CSTS")){
                    if(l.getId().equals(oid)){
                        return new ViewCustomer(l);
                    }
                }
            }
            case "DCSS" -> {
                for(Location l : Engine.getLocations("DCSS")){
                    if(l.getId().equals(oid)){
                        return new ViewLocation(l, desktop);
                    }
                }
            }
            case "VEND" -> {
                for(Location l : Engine.getLocations("VEND")){
                    if(l.getId().equals(oid)){
                        return new ViewLocation(l, desktop);
                    }
                }
            }
            case "ITS" -> {
                Item i = Engine.products.getItem(oid);
                if(i != null){
                    return new ViewItem(i);
                }
            }
            case "USRS" -> {
                User u = Engine.getUser(oid);
                if(u != null){
                    return new ViewUser(desktop, u);
                }
            }
            case "EMPS" -> {
                for(Employee e : Engine.getEmployees()){
                    if(e.getId().equals(oid)){
                        return new ViewEmployee(e);
                    }
                }
            }
            case "MTS" -> {
                return new Materials(desktop);
            }
            case "LGS" -> {
                for(Ledger l : getLedgers()){
                    if(l.getId().equals(oid)){
                        return new ViewLedger(l, desktop);
                    }
                }
                return new Ledgers(desktop);
            }
            case "WHS" -> {
                for(Location l : getLocations("WHS")){
                    if(l.getId().equals(oid)){
                        return new ViewLocation(l, desktop);
                    }
                }
                return new Locations("/WHS", desktop);
            }
            case "ORDS" -> {
                for(PurchaseOrder l : orders.getPurchaseOrder()){
                    if(l.getOrderId().equals(oid)){
                        return new ViewPurchaseOrder(l);
                    }
                }
                return new PurchaseOrders(desktop);
            }
            case "INV" -> {
                return new org.Canal.UI.Views.Controllers.Inventory();
            }
            case "CATS" -> {
                return new Catalogs(desktop);
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

    public static class products {
        public static ArrayList<Item> getProducts() {
            ArrayList<Item> products = new ArrayList<>();
            String[] ps = new String[]{"ITS", "MTS", "CMPS"};
            for(String p : ps) {
                File[] d = Pipe.list(p);
                for (File file : d) {
                    if (!file.isDirectory() && file.getPath().endsWith("." + p.toLowerCase())) {
                        Item loc = Json.load(file.getPath(), Item.class);
                        products.add(loc);
                    }
                }
            }
            products.sort(Comparator.comparing(Item::getId));
            return products;
        }

        public static ArrayList<Item> getItems() {
            ArrayList<Item> items = new ArrayList<>();
            File[] d = Pipe.list("ITS");
            for (File file : d) {
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
            return getItems().stream().filter(i -> i.getId().equals(id)).toList().stream().findFirst().orElse(null);
        }

        public static ArrayList<Item> getMaterials() {
            ArrayList<Item> materials = new ArrayList<>();
            File[] d = Pipe.list("MTS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Item a = Json.load(file.getPath(), Item.class);
                    materials.add(a);
                }
            }
            materials.sort(Comparator.comparing(Item::getId));
            return materials;
        }

        public static Item getMaterial(String id) {
            return getMaterials().stream().filter(m -> m.getId().equals(id)).toList().stream().findFirst().orElse(null);
        }

        public static ArrayList<Item> getComponents() {
            ArrayList<Item> components = new ArrayList<>();
            File[] d = Pipe.list("CMPS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Item a = Json.load(file.getPath(), Item.class);
                    components.add(a);
                }
            }
            components.sort(Comparator.comparing(Item::getId));
            return components;
        }

        public static Item getComponent(String id) {
            return getComponents().stream().filter(c -> c.getId().equals(id)).toList().stream().findFirst().orElse(null);
        }
    }

    public static class orders {

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

        public static PurchaseRequisition getPurchaseRequisition(String id){
            return getPurchaseRequisitions().stream().filter(pr -> pr.getId().equals(id)).toList().stream().findFirst().orElse(null);
        }

        public static ArrayList<PurchaseOrder> getPurchaseOrder() {
            ArrayList<PurchaseOrder> pos = new ArrayList<>();
            File[] posDir = Pipe.list("ORDS/PO");
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
            File[] posDir = Pipe.list("ORDS/PO");
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
            File[] d = Pipe.list("ORDS/SO");
            for (File f : d) {
                if (!f.isDirectory()) {
                    SalesOrder a = Json.load(f.getPath(), SalesOrder.class);
                    if (a.getOrderId().equals(poNumber)) {
                        return a;
                    }
                }
            }
            return null;
        }

        public static ArrayList<PurchaseRequisition> getPurchaseRequisitions() {
            ArrayList<PurchaseRequisition> prs = new ArrayList<>();
            File[] d = Pipe.list("PR");
            for (File f : d) {
                if (!f.isDirectory()) {
                    PurchaseRequisition a = Json.load(f.getPath(), PurchaseRequisition.class);
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
}