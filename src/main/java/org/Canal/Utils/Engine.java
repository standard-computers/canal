package org.Canal.Utils;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.Models.HumanResources.Timesheet;
import org.Canal.Models.HumanResources.User;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.Models.Record;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Start;
import org.Canal.UI.Views.*;
import org.Canal.UI.Views.Areas.*;
import org.Canal.UI.Views.Bins.*;
import org.Canal.UI.Views.Customers.ViewCustomer;
import org.Canal.UI.Views.Departments.DeleteDepartment;
import org.Canal.UI.Views.Finance.Accounts.Accounts;
import org.Canal.UI.Views.Finance.Accounts.CreateAccount;
import org.Canal.UI.Views.Finance.Catalogs.ViewCatalog;
import org.Canal.UI.Views.Finance.GoodsIssues.GoodsIssues;
import org.Canal.UI.Views.Finance.PurchaseOrders.*;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.*;
import org.Canal.UI.Views.Finance.SalesOrders.ViewSalesOrder;
import org.Canal.UI.Views.Items.ModifyItem;
import org.Canal.UI.Views.People.CreatePerson;
import org.Canal.UI.Views.People.People;
import org.Canal.UI.Views.Positions.Positions;
import org.Canal.UI.Views.Productivity.Flows.CreateFlow;
import org.Canal.UI.Views.Productivity.Waves.CreateWave;
import org.Canal.UI.Views.Productivity.WorkOrders.CreateWorkOrder;
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
import org.Canal.UI.Views.Items.CreateItem;
import org.Canal.UI.Views.Items.ViewItem;
import org.Canal.UI.Views.Items.Items;
import org.Canal.UI.Views.Rates.CreateRate;
import org.Canal.UI.Views.Rates.Rates;
import org.Canal.UI.Views.Rates.ViewRate;
import org.Canal.UI.Views.System.CanalSettings;
import org.Canal.UI.Views.System.QuickExplorer;
import org.Canal.UI.Views.Teams.CreateTeam;
import org.Canal.UI.Views.Teams.Teams;
import org.Canal.UI.Views.Inventory.*;
import org.Canal.UI.Views.Finance.Invoices.CreateInvoice;
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
import org.bson.Document;

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
        boolean exit = false;
        if (Engine.assignedUser != null) {
            exit = true;
        }
        Engine.assignedUser = assignedUser;
        configuration.setAssignedUser(assignedUser.getId());
        Pipe.saveConfiguration();
        if (exit) {
            JOptionPane.showMessageDialog(null, "Restart Required!", "You must restart Canal to have your profile take effect.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
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

    /**
     * LOCATIONS
     */
    public static ArrayList<Location> getLocations() {

        ArrayList<Location> locations = new ArrayList<>();
        String[] locs = new String[]{"DCSS", "CCS", "CSTS", "ORGS", "VEND", "WHS", "TRANS/CRRS", "OFFS"};
        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            for (String l : locs) {
                File[] d = Pipe.list(l);
                for (File file : d) {
                    if (!file.isDirectory() && file.getPath().endsWith("." + l.toLowerCase())) {
                        Location loc = Pipe.load(file.getPath(), Location.class);
                        locations.add(loc);
                    }
                }
            }
        } else {

            for (String l : locs) {
                ConnectDB.collection(l).find().forEach(location -> {
                    Location u = Pipe.load(location, Location.class);
                    locations.add(u);
                });
            }
        }

        locations.sort(Comparator.comparing(Location::getId));
        return locations;
    }

    public static Location getLocationWithId(String id) {
        for(Location loc : getLocations()) {
            if(loc.getId().equals(id)) {
                return loc;
            }
        }
        return null;
    }

    public static ArrayList<Location> getLocations(String objex) {

        ArrayList<Location> locations = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list(objex);
            for (File file : d) {
                if (!file.isDirectory()) {
                    Location l = Pipe.load(file.getPath(), Location.class);
                    locations.add(l);
                }
            }
        } else {
            ConnectDB.collection((objex.startsWith("/") ? objex.replaceFirst("/", "") : objex)).find().forEach(location -> {
                Location u = Pipe.load(location, Location.class);
                locations.add(u);
            });
        }

        locations.sort(Comparator.comparing(Location::getId));
        return locations;
    }

    public static List<Location> getLocations(String org, String objex) {
        return getLocations(objex).stream().filter(l -> l.getOrganization().equals(org)).collect(Collectors.toList());
    }

    public static Location getLocation(String id, String objex) {
        for (Location l : getLocations(objex)) {
            if (l.getId().equals(id)) {
                return l;
            }
        }
        return null;
    }

    /**
     * PEOPLE
     */
    public static ArrayList<Employee> getPeople() {

        ArrayList<Employee> people = new ArrayList<>();
        if(Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] areasDir = Pipe.list("PPL");
            for (File file : areasDir) {
                if (!file.isDirectory()) {
                    Employee person = Pipe.load(file.getPath(), Employee.class);
                    people.add(person);
                }
            }
        }else{
            ConnectDB.collection("PPL").find().forEach(person -> {
                Employee ep = Pipe.load(person, Employee.class);
                people.add(ep);
            });
        }
        people.sort(Comparator.comparing(Employee::getId));
        return people;
    }

    /**
     * AREAS
     */
    public static ArrayList<Area> getAreas() {

        ArrayList<Area> areas = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] areasDir = Pipe.list("AREAS");
            for (File file : areasDir) {
                if (!file.isDirectory()) {
                    Area area = Pipe.load(file.getPath(), Area.class);
                    areas.add(area);
                }
            }
        } else {
            ConnectDB.collection("AREAS").find().forEach(area -> {
                Area u = Pipe.load(area, Area.class);
                areas.add(u);
            });
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

    /**
     * BINS
     */
    public static Bin getBin(String id) {
        ArrayList<Area> areas = getAreas();
        for (Area area : areas) {
            for (Bin b : area.getBins()) {
                if (b.getId().equals(id)) {
                    return b;
                }
            }
        }
        return null;
    }

    public static ArrayList<Bin> getBins(int limit) {
        ArrayList<Bin> bins = new ArrayList<>();
        if (limit == 0) return bins;
        final boolean unlimited = limit < 0;
        outer:
        for (Area area : getAreas()) {
            if (area == null) continue;
            java.util.List<Bin> areaBins = area.getBins();
            if (areaBins == null || areaBins.isEmpty()) continue;
            for (Bin b : areaBins) {
                b.setArea(area.getId());
                bins.add(b);
                if (!unlimited && bins.size() >= limit) {
                    break outer;
                }
            }
        }
        return bins;
    }

    /**
     * EMPLOYEES
     */
    public static ArrayList<Employee> getEmployees() {

        ArrayList<Employee> employees = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] d = Pipe.list("EMPS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Employee a = Pipe.load(file.getPath(), Employee.class);
                    employees.add(a);
                }
            }
        } else {

            ConnectDB.collection("EMPS").find().forEach(employee -> {
                Employee e = Pipe.load(employee, Employee.class);
                employees.add(e);
            });
        }

        employees.sort(Comparator.comparing(Employee::getId));
        return employees;
    }

    public static List<Employee> getEmployees(String id) {
        return getEmployees().stream().filter(e -> e.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static Employee getEmployee(String id) {
        return getEmployees().stream().filter(e -> e.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * TIME SHEETS
     */
    public static ArrayList<Timesheet> getTimesheets() {
        ArrayList<Timesheet> timesheets = new ArrayList<>();
        File[] d = Pipe.list("HR/TMSH");
        for (File file : d) {
            if (!file.isDirectory()) {
                Timesheet a = Pipe.load(file.getPath(), Timesheet.class);
                timesheets.add(a);
            }
        }
        timesheets.sort(Comparator.comparing(Timesheet::getId));
        return timesheets;
    }

    public static Timesheet getTimesheet(String employeeId) {
        var i = getTimesheets().stream().filter(e -> e.getEmployee().equals(employeeId)).toList().stream().findFirst().orElse(null);
        if (i == null) {
            i = new Timesheet(employeeId);
            Pipe.save("/HR/TMSH", i);
        }
        return i;
    }

    /**
     * CATALOGS
     */
    public static ArrayList<Catalog> getCatalogs() {

        ArrayList<Catalog> catalogs = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] catsDirs = Pipe.list("CATS");
            for (File catsDir : catsDirs) {
                if (!catsDir.isDirectory()) {
                    Catalog catalog = Pipe.load(catsDir.getPath(), Catalog.class);
                    catalogs.add(catalog);
                }
            }
        } else {

            ConnectDB.collection("CATS").find().forEach(catalog -> {
                Catalog u = Pipe.load(catalog, Catalog.class);
                catalogs.add(u);
            });
        }

        catalogs.sort(Comparator.comparing(Catalog::getId));
        return catalogs;
    }

    public static Catalog getCatalog(String id) {
        return getCatalogs().stream().filter(c -> c.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * USERS
     */
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();

        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] usrsDir = Pipe.list("USRS");
            for (File file : usrsDir) {
                if (!file.isDirectory()) {
                    User a = Pipe.load(file.getPath(), User.class);
                    users.add(a);
                }
            }
            users.sort(Comparator.comparing(User::getId));
        } else {

            ConnectDB.collection("USRS").find().forEach(user -> {
                User u = Pipe.load(user, User.class);
                users.add(u);
            });
        }

        return users;
    }

    public static User getUser(String id) {
        return getUsers().stream().filter(u -> u.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * PURCHASE ORDERS
     */
    public static ArrayList<PurchaseOrder> getPurchaseOrders() {

        ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] posDir = Pipe.list("ORDS/PO");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    PurchaseOrder purchaseOrder = Pipe.load(file.getPath(), PurchaseOrder.class);
                    purchaseOrders.add(purchaseOrder);
                }
            }
        } else {
            ConnectDB.collection("ORDS/PO").find().forEach(purchaseOrder -> {
                PurchaseOrder u = Pipe.load(purchaseOrder, PurchaseOrder.class);
                purchaseOrders.add(u);
            });
        }

        purchaseOrders.sort(Comparator.comparing(PurchaseOrder::getId));
        return purchaseOrders;
    }

    public static PurchaseOrder getPurchaseOrder(String purchaseOrderId) {
        return getPurchaseOrders().stream().filter(pr -> pr.getId().equals(purchaseOrderId)).toList().stream().findFirst().orElse(null);
    }

    public static List<PurchaseOrder> getPurchaseOrders(String shipTo) {
        return getPurchaseOrders().stream().filter(order -> order.getShipTo().equals(shipTo)).collect(Collectors.toList());
    }

    public static List<PurchaseOrder> getPurchaseOrders(String shipTo, LockeStatus status) {
        return getPurchaseOrders().stream().filter(order -> order.getShipTo().equals(shipTo) && order.getStatus().equals(status)).collect(Collectors.toList());
    }

    /**
     * LEDGERS
     */
    public static ArrayList<Ledger> getLedgers() {
        ArrayList<Ledger> ledgers = new ArrayList<>();

        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] d = Pipe.list("LGS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Ledger a = Pipe.load(file.getPath(), Ledger.class);
                    ledgers.add(a);
                }
            }
        } else {

            ConnectDB.collection("LGS").find().forEach(ledger -> {
                Ledger u = Pipe.load(ledger, Ledger.class);
                ledgers.add(u);
            });
        }

        ledgers.sort(Comparator.comparing(Ledger::getId));
        return ledgers;
    }

    public static Ledger getLedger(String id) {
        return getLedgers().stream().filter(inventory -> inventory.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * RATES
     */
    public static ArrayList<Rate> getRates() {

        ArrayList<Rate> rates = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] d = Pipe.list("RTS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Rate rate = Pipe.load(file.getPath(), Rate.class);
                    rates.add(rate);
                }
            }
        } else {

            ConnectDB.collection("RTS").find().forEach(rate -> {
                Rate r = Pipe.load(rate, Rate.class);
                rates.add(r);
            });
        }

        rates.sort(Comparator.comparing(Rate::getId));
        return rates;
    }

    public static Rate getRate(String id) {
        return getRates().stream().filter(rate -> rate.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * TRUCKS
     */
    public static ArrayList<Truck> getTrucks() {

        ArrayList<Truck> trucks = new ArrayList<>();
        if(Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list("TRANS/TRCKS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Truck a = Pipe.load(file.getPath(), Truck.class);
                    trucks.add(a);
                }
            }
        } else {
            ConnectDB.collection("TRANS/TRCKS").find().forEach(truck -> {
                Truck u = Pipe.load(truck, Truck.class);
                trucks.add(u);
            });
        }

        trucks.sort(Comparator.comparing(Truck::getId));
        return trucks;
    }

    /**
     * POSITIONS
     */
    public static ArrayList<Position> getPositions() {

        ArrayList<Position> positions = new ArrayList<>();
        if(Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list("HR/POS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Position position = Pipe.load(file.getPath(), Position.class);
                    positions.add(position);
                }
            }
        } else {
            ConnectDB.collection("HR/POS").find().forEach(position -> {
                Position u = Pipe.load(position, Position.class);
                positions.add(u);
            });
        }

        positions.sort(Comparator.comparing(Position::getId));
        return positions;
    }

    public static Position getPosition(String id) {
        return getPositions().stream().filter(position -> position.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * INBOUND DELIVERIES
     */
    public static ArrayList<Delivery> getInboundDeliveries() {

        ArrayList<Delivery> inboundDeliveries = new ArrayList<>();
        if(Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list("TRANS/IDO");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Delivery inboundDelivery = Pipe.load(file.getPath(), Delivery.class);
                    inboundDeliveries.add(inboundDelivery);
                }
            }
        } else {
            ConnectDB.collection("TRANS/IDO").find().forEach(inboundDelivery -> {
                Delivery delivery = Pipe.load(inboundDelivery, Delivery.class);
                inboundDeliveries.add(delivery);
            });
        }

        inboundDeliveries.sort(Comparator.comparing(Delivery::getId));
        return inboundDeliveries;
    }

    public static ArrayList<Delivery> getInboundDeliveries(String destination) {
        ArrayList<Delivery> inboundDeliveries = new ArrayList<>();
        for(Delivery delivery : getInboundDeliveries()){
            if(delivery.getDestination().equals(destination)){
                inboundDeliveries.add(delivery);
            }
        }
        return inboundDeliveries;
    }

    public static Delivery getInboundDelivery(String id) {
        return getInboundDeliveries().stream().filter(delivery -> delivery.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * OUTBOUND DELIVERIES
     */
    public static ArrayList<Delivery> getOutboundDeliveries() {

        ArrayList<Delivery> outboundDeliveries = new ArrayList<>();
        if(Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list("TRANS/ODO");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Delivery outboundDelivery = Pipe.load(file.getPath(), Delivery.class);
                    outboundDeliveries.add(outboundDelivery);
                }
            }
        } else {
            ConnectDB.collection("TRANS/ODO").find().forEach(outboundDelivery -> {
                Delivery delivery = Pipe.load(outboundDelivery, Delivery.class);
                outboundDeliveries.add(delivery);
            });
        }

        outboundDeliveries.sort(Comparator.comparing(Delivery::getId));
        return outboundDeliveries;
    }

    public static ArrayList<Delivery> getOutboundDeliveries(String destination) {

        ArrayList<Delivery> outboundDeliveries = new ArrayList<>();
        for(Delivery delivery : getOutboundDeliveries()){
            if(delivery.getDestination().equals(destination)){
                outboundDeliveries.add(delivery);
            }
        }
        return outboundDeliveries;
    }

    public static Delivery getOutboundDelivery(String id) {
        return getOutboundDeliveries().stream().filter(delivery -> delivery.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * INVENTORIES
     */
    public static ArrayList<Inventory> getInventories() {

        ArrayList<Inventory> is = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] d = Pipe.list("STK");
            for (File f : d) {
                if (!f.isDirectory()) {
                    Inventory a = Pipe.load(f.getPath(), Inventory.class);
                    is.add(a);
                }
            }
        } else {

            ConnectDB.collection("STK").find().forEach(stock -> {
                Inventory u = Pipe.load(stock, Inventory.class);
                is.add(u);
            });
        }

        is.sort(Comparator.comparing(Inventory::getLocation));
        return is;
    }

    public static Inventory getInventory(String location) {
        var i = getInventories().stream().filter(inventory -> inventory.getLocation().equals(location)).toList().stream().findFirst().orElse(null);
        if (i == null) {
            if(getConfiguration().getMongodb().isEmpty()) {
                i = new Inventory(location);
                Pipe.save("/STK", i);
            }
        }
        return i;
    }

    /**
     * RECORDS
     */
    public static ArrayList<ArrayList<Record>> getRecords() {

        ArrayList<ArrayList<Record>> all = new ArrayList<>();
        if(Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list("RCS");
            for (File f : d) {
                if (!f.isDirectory()) {
                    all.add(Pipe.load(f.getPath(), ArrayList.class));
                }
            }
        }else {
            ConnectDB.collection("RCS").find().forEach(records -> {
               ArrayList<Record> r = Pipe.load(records, ArrayList.class);
               all.add(r);
            });
        }

        return all;
    }

    public static void assertRecord(String objex, String id, Record record) {
        ArrayList<Record> rcs = null;
        File[] d = Pipe.list("RCS");
        for (File f : d) {
            if (!f.isDirectory()) {
                if (f.getPath().endsWith(id + "." + objex.toLowerCase().replaceAll("/", "."))) {
                    rcs = Pipe.load(f.getPath(), ArrayList.class);
                }
            }
        }
        if (rcs != null) {
            rcs.add(record);
        }
        Pipe.export(Start.DIR + "\\.store\\RCS\\" + id + "." + objex.toLowerCase().replaceAll("/", "."), rcs);
    }

    public static Object codex(String objex, String key) {
        return (Engine.codex.getValue(objex, key) == null ? false : Engine.codex.getValue(objex, key));
    }

    public static String generateId(String objexType){

        int objexCount = (int) ConnectDB.collection(objexType).countDocuments();
        String prefix = (String) Engine.codex.getValue(objexType, "prefix");
        int leadingZeros = (Integer) Engine.codex.getValue(objexType, "leading_zeros"); // e.g., 3 -> 001
        int nextId = objexCount + 1;
        int width = Math.max(0, leadingZeros);
        String numberPart = String.format("%0" + width + "d", nextId); // zero-pad to width
        return prefix + numberPart;
    }

    public static JInternalFrame router(String transactionCode, DesktopState desktop) {
        transactionCode = transactionCode.toUpperCase().trim();
        if (!transactionCode.startsWith("/")) {
            transactionCode = "/" + transactionCode;
        }
        switch (transactionCode) {

            //Accounts
            case "/ACCS" -> {
                return new Accounts(desktop);
            }
            case "/ACCS/NEW" -> {
                return new CreateAccount();
            }

            //AREAS
            case "/AREAS" -> {
                return new Areas(desktop);
            }
            case "/AREAS/NEW" -> {
                return new CreateArea(null, desktop, null);
            }
            case "/AREAS/AUTO_MK" -> {
                return new AutoMakeAreas(null);
            }
            case "/AREAS/O" -> {
                String areaId = JOptionPane.showInputDialog(null, "Enter Area ID", "Area ID", JOptionPane.QUESTION_MESSAGE);
                Area area = Engine.getArea(areaId);
                return new ViewArea(area, desktop, null);
            }

            //BINS
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
            case "/BNS/O" -> {
                String binId = JOptionPane.showInputDialog(null, "Enter Bin ID", "Bin ID");
                Bin bin = Engine.getBin(binId);
                return new ViewBin(bin, desktop, null);
            }

            //ORGANIZATIONS
            case "/ORGS" -> {
                return new Locations("/ORGS", desktop);
            }
            case "/ORGS/NEW" -> {
                return new CreateLocation("/ORGS", desktop, null);
            }
            case "/ORGS/O" -> {
                String organizationId = JOptionPane.showInputDialog(null, "Enter Organization ID", "Organization ID", JOptionPane.QUESTION_MESSAGE);
                Location organization = Engine.getLocation(organizationId, "CSTS");
                return new ViewLocation(organization, desktop);
            }

            //COST CENTERS
            case "/CCS" -> {
                return new Locations("/CCS", desktop);
            }
            case "/CCS/NEW" -> {
                return new CreateLocation("/CCS", desktop, null);
            }
            case "/CCS/O" -> {
                String costCenterId = JOptionPane.showInputDialog(null, "Enter Cost Center ID", "Cost Center ID", JOptionPane.QUESTION_MESSAGE);
                Location costCenter = Engine.getLocation(costCenterId, "CCS");
                return new ViewLocation(costCenter, desktop);
            }

            //CUSTOMERS
            case "/CSTS" -> {
                return new Locations("/CSTS", desktop);
            }
            case "/CSTS/NEW" -> {
                return new CreateLocation("/CSTS", desktop, null);
            }
            case "/CSTS/O" -> {
                String customerId = JOptionPane.showInputDialog(null, "Enter Customer ID", "Customer ID", JOptionPane.QUESTION_MESSAGE);
                Location customer = Engine.getLocation(customerId, "CSTS");
                return new ViewCustomer(customer);
            }

            //DISTRIBUTION CENTERS
            case "/DCSS" -> {
                return new Locations("/DCSS", desktop);
            }
            case "/DCSS/NEW" -> {
                return new CreateLocation("/DCSS", desktop, null);
            }

            //OFFICES
            case "/OFFS" -> {
                return new Locations("/OFFS", desktop);
            }
            case "/OFFS/NEW" -> {
                return new CreateLocation("/OFFS", desktop, null);
            }

            //TRANSPORTATION CARRIERS
            case "/TRANS/CRRS" -> {
                return new Locations("/TRANS/CRRS", desktop);
            }
            case "/TRANS/CRRS/NEW" -> {
                return new CreateLocation("/TRANS/CRRS", desktop, null);
            }

            //OUTBOUND DELIVERIES
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

            //TRUCKS
            case "/TRANS/TRCKS" -> {
                return new Trucks(desktop);
            }
            case "/TRANS/TRCKS/NEW" -> {
                return new CreateTruck(desktop);
            }

            //WAREHOUSES
            case "/WHS" -> {
                return new Locations("/WHS", desktop);
            }
            case "/WHS/NEW" -> {
                return new CreateLocation("/WHS", desktop, null);
            }

            //VENDORS
            case "/VEND" -> {
                return new Locations("/VEND", desktop);
            }
            case "/VEND/NEW" -> {
                return new CreateLocation("/VEND", desktop, null);
            }

            //VALUE ADDED SERVICES
            case "/VAS" -> {
                return new ValueAddedServices();
            }
            case "/VAS/NEW" -> {
                return new CreateVAS();
            }
            case "/VAS/O" -> {
                String vasId = JOptionPane.showInputDialog(null, "Enter VAS ID", "VAS", JOptionPane.QUESTION_MESSAGE);
//                Engine.getVa
                return new CreateVAS();
            }

            //RATES
            case "/RTS" -> {
                return new Rates(desktop);
            }
            case "/RTS/NEW" -> {
                return new CreateRate(desktop, null);
            }
            case "/RTS/O" -> {
                String rateId = JOptionPane.showInputDialog(null, "Enter Rate ID", "Rate ID", JOptionPane.QUESTION_MESSAGE);
                Rate rate = Engine.getRate(rateId);
                return new ViewRate(rate, desktop, null);
            }

            //LEDGERS
            case "/LGS" -> {
                return new Ledgers(desktop);
            }
            case "/LGS/NEW" -> {
                return new CreateLedger(desktop);
            }
            case "/LGS/O" -> {
                String ledgerId = JOptionPane.showInputDialog(null, "Enter Ledger ID", "Ledger ID", JOptionPane.QUESTION_MESSAGE);
                Ledger ledger = Engine.getLedger(ledgerId);
                return new ViewLedger(ledger, desktop);
            }

            //EMPLOYEES
            case "/EMPS" -> {
                return new Employees(desktop);
            }
            case "/EMPS/O" -> {
                String eid = JOptionPane.showInputDialog(null, "Enter Employee ID", "Employee ID", JOptionPane.QUESTION_MESSAGE);
                Employee employee = Engine.getEmployee(eid);
                return new ViewEmployee(employee, desktop, null);
            }
            case "/EMPS/NEW" -> {
                return new CreateEmployee(desktop, null);
            }

            //PEOPLE
            case "/PPL" -> {
                return new People(desktop);
            }
            case "/PPL/O" -> {
                String pid = JOptionPane.showInputDialog(null, "Enter Person ID", "Person ID", JOptionPane.QUESTION_MESSAGE);
                Employee person = Engine.getEmployee(pid);
                return new ViewEmployee(person, desktop, null);
            }
            case "/PPL/NEW" -> {
                return new CreatePerson(desktop, false);
            }

            //DEPARTMENTS
            case "/DPTS" -> {
                return new Departments(desktop);
            }
            case "/DPTS/NEW" -> {
                return new CreateDepartment(desktop, null);
            }
            case "/DPTS/DEL" -> {
                return new DeleteDepartment();
            }

            //TEAMS
            case "/TMS" -> {
                return new Teams();
            }
            case "/TMS/NEW" -> {
                return new CreateTeam();
            }

            //USERS
            case "/USRS" -> {
                return new Users(desktop);
            }
            case "/USRS/O" -> {
                String uId = JOptionPane.showInputDialog(null, "Enter User ID", "User ID", JOptionPane.QUESTION_MESSAGE);
                User user = Engine.getUser(uId);
                return new ViewUser(desktop, user);
            }
            case "/USRS/CHG_PSSWD" -> {
                return new ChangeUserPassword();
            }
            case "/USRS/NEW" -> {
                return new CreateUser(desktop, null);
            }

            //STOCK AND INVENTORY
            case "/STK", "/INV" -> {
                return new ViewInventory(desktop, Engine.getOrganization().getId());
            }
            case "/STK/MOD/MV" -> {
                return new MoveStock("", null);
            }

            //INVOICES
            case "/INVS", "/INVS/NEW" -> {
                return new CreateInvoice(null);
            }

            //CATALOGS
            case "/CATS" -> {
                return new Catalogs(desktop);
            }
            case "/CATS/NEW" -> {
                return new CreateCatalog(null);
            }
            case "/CATS/O" -> {
                String catId = JOptionPane.showInputDialog(null, "Enter Catalog ID", "Catalog ID", JOptionPane.QUESTION_MESSAGE);
                Catalog catalog = Engine.getCatalog(catId);
                return new ViewCatalog(catalog);
            }

            //ITEMS
            case "/ITS" -> {
                return new Items(desktop);
            }
            case "/ITS/F" -> {
                return new Items(desktop);
            }
            case "/ITS/NEW" -> {
                return new CreateItem(desktop, null);
            }
            case "/ITS/MOD" -> {
                String eid = JOptionPane.showInputDialog(null, "Enter Item ID", "Item ID", JOptionPane.QUESTION_MESSAGE);
                Item i = Engine.getItem(eid);
                return new ModifyItem(i, null);
            }
            case "/ITS/O" -> {
                String eid = JOptionPane.showInputDialog(null, "Enter Item ID", "Item ID", JOptionPane.QUESTION_MESSAGE);
                Item i = Engine.getItem(eid);
                return new ViewItem(i, desktop, null);
            }

            //PURCHASE ORDERS
            case "/ORDS/PO" -> {
                return new PurchaseOrders(desktop);
            }
            case "/ORDS/PO/NEW" -> {
                return new CreatePurchaseOrder(desktop);
            }
            case "/ORDS/PO/AUTO_MK" -> {
                return new AutoMakePurchaseOrders(desktop);
            }
            case "/ORDS/PO/O" -> {
                String poId = JOptionPane.showInputDialog(null, "Enter Purchase Order ID", "Purchase Order ID", JOptionPane.QUESTION_MESSAGE);
                PurchaseOrder purchaseOrder = Engine.getPurchaseOrder(poId);
                return new ViewPurchaseOrder(purchaseOrder, desktop, null);
            }
            case "/ORDS/RCV" -> {
                return new ReceiveOrder(Engine.getOrganization().getId(), desktop, null);
            }
            case "/ORDS/RTRN" -> {
                return new ReturnOrder(desktop);
            }

            //PURCHASE REQUISITIONS
            case "/ORDS/PR" -> {
                return new PurchaseRequisitions(desktop);
            }
            case "/ORDS/PR/NEW" -> {
                return new CreatePurchaseRequisition();
            }
            case "/ORDS/PR/PO" -> {
                return new ConvertPurchaseRequisitions(desktop);
            }
            case "/ORDS/PR/O" -> {
                String poId = JOptionPane.showInputDialog(null, "Enter Purchase Requisition ID", "Purchase Requisition ID", JOptionPane.QUESTION_MESSAGE);
                PurchaseRequisition purchaseRequisition = Engine.getPurchaseRequisition(poId);
                return new ViewPurchaseRequisition(purchaseRequisition, desktop, null);
            }
            case "/ORDS/PR/AUTO_MK" -> {
                return new AutoMakePurchaseRequisitions(desktop);
            }

            //SALES ORDERS
            case "/ORDS/SO" -> {
                return new SalesOrders(desktop);
            }
            case "/ORDS/SO/NEW" -> {
                return new CreateSalesOrder();
            }
            case "/ORDS/SO/O" -> {
                String soId = JOptionPane.showInputDialog(null, "Enter Sales Order ID", "Sales Order ID", JOptionPane.QUESTION_MESSAGE);
                SalesOrder salesOrder = Engine.getSalesOrder(soId);
                return new ViewSalesOrder(salesOrder);
            }
            case "/ORDS/SO/AUTO_MK" -> {
                return new AutoMakeSalesOrders();
            }

            //GOODS RECEIPTS
            case "/GR" -> {
                return new GoodsReceipts(desktop);
            }

            //GOODS ISSUES
            case "/GI" -> {
                return new GoodsIssues(desktop);
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
                return new ReceiveOrder("", desktop, null);
            }

            //NOTES
            case "/NTS" -> {
                return new Notes();
            }
            case "/NTS/NEW" -> {
                return new CreateNote();
            }

            //MOVEMENTS AND TASKS
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

            //POSITIONS
            case "/HR/POS" -> {
                return new Positions(desktop);
            }
            case "/HR/POS/NEW" -> {
                return new CreatePosition(desktop, null);
            }

            //CONTROLLERS
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
                return new CanalSettings(desktop);
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
        if (transactionCode.endsWith("/F")) {
            return new Finder(transactionCode.replace("/F", ""), desktop);
        } else if (transactionCode.endsWith("/MOD")) {
            return new Modifier(transactionCode.replace("/MOD", ""), null, null);
        } else if (transactionCode.endsWith("/ARCHV")) {
            return new Archiver(transactionCode.replace("/ARCHV", ""));
        } else if (transactionCode.endsWith("/DEL")) {
            return new Deleter(transactionCode.replace("/DEL", ""), null);
        }

        String[] chs = transactionCode.split("/");
        String t = chs[1];
        String oid = chs[2];
        switch (t) {
            case "ORGS" -> {
                for (Location org : Engine.getLocations("ORGS")) {
                    if (org.getId().equals(oid)) {
                        return new ViewLocation(org, desktop);
                    }
                }
            }
            case "AREAS" -> {
                for (Area l : Engine.getAreas()) {
                    if (l.getId().equals(oid)) {

                    }
                }
            }
            case "CCS" -> {
                for (Location l : Engine.getLocations("CCS")) {
                    if (l.getId().equals(oid)) {
                        return new ViewLocation(l, desktop);
                    }
                }
            }
            case "CSTS" -> {
                for (Location l : Engine.getLocations("CSTS")) {
                    if (l.getId().equals(oid)) {
                        return new ViewCustomer(l);
                    }
                }
            }
            case "DCSS" -> {
                for (Location l : Engine.getLocations("DCSS")) {
                    if (l.getId().equals(oid)) {
                        return new ViewLocation(l, desktop);
                    }
                }
            }
            case "VEND" -> {
                for (Location l : Engine.getLocations("VEND")) {
                    if (l.getId().equals(oid)) {
                        return new ViewLocation(l, desktop);
                    }
                }
            }
            case "ITS" -> {
                Item i = Engine.getItem(oid);
                if (i != null) {
                    return new ViewItem(i, desktop, null);
                }
            }
            case "USRS" -> {
                User u = Engine.getUser(oid);
                if (u != null) {
                    return new ViewUser(desktop, u);
                }
            }
            case "EMPS" -> {
                for (Employee e : Engine.getEmployees()) {
                    if (e.getId().equals(oid)) {
                        return new ViewEmployee(e, desktop, null);
                    }
                }
            }
            case "LGS" -> {
                for (Ledger l : getLedgers()) {
                    if (l.getId().equals(oid)) {
                        return new ViewLedger(l, desktop);
                    }
                }
                return new Ledgers(desktop);
            }
            case "WHS" -> {
                for (Location l : getLocations("WHS")) {
                    if (l.getId().equals(oid)) {
                        return new ViewLocation(l, desktop);
                    }
                }
                return new Locations("/WHS", desktop);
            }
            case "ORDS" -> {
                for (PurchaseOrder l : getPurchaseOrders()) {
                    if (l.getOrderId().equals(oid)) {
                        return new ViewPurchaseOrder(l, desktop, null);
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

    /**
     * ITEMS
     */
    public static ArrayList<Item> getItems() {

        ArrayList<Item> items = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) { //Local disk

            File[] d = Pipe.list("ITS");
            for (File file : d) {
                if (!file.isDirectory()) {
                    Item item = Pipe.load(file.getPath(), Item.class);
                    items.add(item);
                }
            }
        } else {

            ConnectDB.collection("ITS").find().forEach(item -> {
                Item u = Pipe.load(item, Item.class);
                items.add(u);
            });
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

    public static PurchaseRequisition getPurchaseRequisition(String id) {
        return getPurchaseRequisitions().stream().filter(pr -> pr.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<SalesOrder> getSalesOrders() {

        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] posDir = Pipe.list("ORDS/SO");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    SalesOrder salesOrder = Pipe.load(file.getPath(), SalesOrder.class);
                    salesOrders.add(salesOrder);
                }
            }
        } else {
            ConnectDB.collection("ORDS/SO").find().forEach(salesOrder -> {
                SalesOrder so = Pipe.load(salesOrder, SalesOrder.class);
                salesOrders.add(so);
            });
        }

        salesOrders.sort(Comparator.comparing(SalesOrder::getOrderId));
        return salesOrders;
    }

    public static SalesOrder getSalesOrder(String salesOrderId) {
        return getSalesOrders().stream().filter(pr -> pr.getId().equals(salesOrderId)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<PurchaseRequisition> getPurchaseRequisitions() {

        ArrayList<PurchaseRequisition> purchaseRequisions = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] d = Pipe.list("ORDS/PR");
            for (File f : d) {
                if (!f.isDirectory()) {
                    PurchaseRequisition purchaseRequisition = Pipe.load(f.getPath(), PurchaseRequisition.class);
                    purchaseRequisions.add(purchaseRequisition);
                }
            }
        } else {
            ConnectDB.collection("ORDS/PR").find().forEach(purchaseRequisition -> {
                PurchaseRequisition pr = Pipe.load(purchaseRequisition, PurchaseRequisition.class);
                purchaseRequisions.add(pr);
            });
        }

        purchaseRequisions.sort(Comparator.comparing(PurchaseRequisition::getId));
        return purchaseRequisions;
    }

    public static PurchaseRequisition getPurchaseRequisitions(String purchaseRequisitionId) {
        return getPurchaseRequisitions().stream().filter(pr -> pr.getId().equals(purchaseRequisitionId)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<GoodsReceipt> getGoodsReceipts() {

        ArrayList<GoodsReceipt> goodsReceipts = new ArrayList<>();
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File[] posDir = Pipe.list("GR");
            for (File file : posDir) {
                if (!file.isDirectory()) {
                    GoodsReceipt a = Pipe.load(file.getPath(), GoodsReceipt.class);
                    goodsReceipts.add(a);
                }
            }
        } else {
            ConnectDB.collection("GR").find().forEach(goodsReceipt -> {
                GoodsReceipt u = Pipe.load(goodsReceipt, GoodsReceipt.class);
                goodsReceipts.add(u);
            });
        }

        goodsReceipts.sort(Comparator.comparing(GoodsReceipt::getId));
        return goodsReceipts;
    }
}