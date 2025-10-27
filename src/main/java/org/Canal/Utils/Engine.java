package org.Canal.Utils;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.ReplaceOptions;
import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.HumanResources.*;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.Models.Record;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Start;
import org.Canal.UI.Views.*;
import org.Canal.UI.Views.Areas.*;
import org.Canal.UI.Views.BOMS.BOMs;
import org.Canal.UI.Views.BOMS.CreateBOM;
import org.Canal.UI.Views.BOMS.ViewBOM;
import org.Canal.UI.Views.Bins.*;
import org.Canal.UI.Views.Customers.ViewCustomer;
import org.Canal.UI.Views.Departments.DeleteDepartment;
import org.Canal.UI.Views.Employees.ModifyEmployee;
import org.Canal.UI.Views.Accounts.Accounts;
import org.Canal.UI.Views.Accounts.AutoMakeAccounts;
import org.Canal.UI.Views.Accounts.CreateAccount;
import org.Canal.UI.Views.Accounts.ViewAccount;
import org.Canal.UI.Views.Catalogs.ViewCatalog;
import org.Canal.UI.Views.GoodsIssues.GoodsIssues;
import org.Canal.UI.Views.Invoices.Invoices;
import org.Canal.UI.Views.Ledgers.AutoMakeLedgers;
import org.Canal.UI.Views.PurchaseOrders.AutoMakePurchaseOrders;
import org.Canal.UI.Views.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.UI.Views.PurchaseOrders.PurchaseOrders;
import org.Canal.UI.Views.PurchaseOrders.ViewPurchaseOrder;
import org.Canal.UI.Views.PurchaseRequisitions.*;
import org.Canal.UI.Views.SalesOrders.ViewSalesOrder;
import org.Canal.UI.Views.Items.ModifyItem;
import org.Canal.UI.Views.People.CreatePerson;
import org.Canal.UI.Views.People.People;
import org.Canal.UI.Views.Positions.Positions;
import org.Canal.UI.Views.Flows.CreateFlow;
import org.Canal.UI.Views.Waves.CreateWave;
import org.Canal.UI.Views.Productivity.WorkOrders.CreateWorkOrder;
import org.Canal.UI.Views.Controllers.*;
import org.Canal.UI.Views.Employees.CreateEmployee;
import org.Canal.UI.Views.Employees.ViewEmployee;
import org.Canal.UI.Views.Employees.Employees;
import org.Canal.UI.Views.Catalogs.Catalogs;
import org.Canal.UI.Views.Catalogs.CreateCatalog;
import org.Canal.UI.Views.GoodsReceipts.GoodsReceipts;
import org.Canal.UI.Views.Ledgers.CreateLedger;
import org.Canal.UI.Views.Ledgers.ViewLedger;
import org.Canal.UI.Views.Ledgers.Ledgers;
import org.Canal.UI.Views.Departments.CreateDepartment;
import org.Canal.UI.Views.Departments.Departments;
import org.Canal.UI.Views.Deliveries.CreateInboundDeliveryOrder;
import org.Canal.UI.Views.Deliveries.InboundDeliveries;
import org.Canal.UI.Views.Notes.CreateNote;
import org.Canal.UI.Views.Notes.Notes;
import org.Canal.UI.Views.Deliveries.CreateOutboundDeliveryOrder;
import org.Canal.UI.Views.Deliveries.OutboundDeliveries;
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
import org.Canal.UI.Views.Invoices.CreateInvoice;
import org.Canal.UI.Views.SalesOrders.AutoMakeSalesOrders;
import org.Canal.UI.Views.SalesOrders.CreateSalesOrder;
import org.Canal.UI.Views.SalesOrders.SalesOrders;
import org.Canal.UI.Views.Tasks.CreateTask;
import org.Canal.UI.Views.Tasks.TaskList;
import org.Canal.UI.Views.Trucks.CreateTruck;
import org.Canal.UI.Views.Trucks.Trucks;
import org.Canal.UI.Views.Users.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;

/**
 * This class is responsible for fetching and maintaining Objex.
 */
public class Engine {

    private static Configuration configuration = new Configuration();
    public static Codex codex;
    public static Location organization;
    public static String location;
    public static User assignedUser;
    private static Gson gson = new Gson();

    private static <T> ArrayList<T> loadCollection(String collectionName, Class<T> clazz) {
        ArrayList<T> results = new ArrayList<>();
        ConnectDB.collection(collectionName).find().forEach(doc -> results.add(Pipe.load(doc, clazz)));
        return results;
    }

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

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        Engine.location = location;
    }

    /**
     * LOCATIONS
     */
    public static ArrayList<Location> getLocations() {

        ArrayList<Location> locations = new ArrayList<>();
        String[] locs = new String[]{"DCSS", "CCS", "CSTS", "ORGS", "VEND", "WHS", "TRANS/CRRS", "OFFS"};
        for (String l : locs) {
            ConnectDB.collection(l).find().forEach(location -> {
                Location u = Pipe.load(location, Location.class);
                locations.add(u);
            });
        }

        locations.sort(Comparator.comparing(Location::getId));
        return locations;
    }


    public static Location getLocationWithId(String id) {

        if (id == null || id.isBlank()) return null;

        String[] collNames = {"DCSS","CCS","CSTS","ORGS","VEND","WHS","TRANS/CRRS","OFFS"};
        for (String name : collNames) {
            MongoCollection<Document> coll = ConnectDB.collection(name);
            if (coll == null) continue;

            Document doc = coll.find(new Document("id", id)).first();
            if (doc != null) {
                return Pipe.load(doc, Location.class);
            }
        }
        return null;
    }

    public static ArrayList<Location> getLocations(String objex) {

        ArrayList<Location> locations = new ArrayList<>();
        ConnectDB.collection((objex.startsWith("/") ? objex.replaceFirst("/", "") : objex)).find().forEach(location -> {
            Location u = Pipe.load(location, Location.class);
            locations.add(u);
        });

        locations.sort(Comparator.comparing(Location::getId));
        return locations;
    }

    public static List<Location> getLocations(String org, String objex) {
        return getLocations(objex).stream().filter(l -> l.getOrganization().equals(org)).collect(Collectors.toList());
    }

    public static Location getLocation(String id, String objex) {

        if (id == null || id.isBlank()) return null;

        if (Engine.getConfiguration().getMongodb().isEmpty()) {
            for (Location l : getLocations(objex)) {
                if (id.equals(l.getId())) return l;
            }
            return null;
        }

        MongoCollection<Document> coll = ConnectDB.collection(objex);
        if (coll == null) return null;

        Document doc = coll.find(new Document("id", id)).first();
        return (doc != null) ? Pipe.load(doc, Location.class) : null;
    }


    /**
     * PEOPLE
     */
    public static ArrayList<Employee> getPeople() {

        ArrayList<Employee> people = new ArrayList<>();
        ConnectDB.collection("PPL").find().forEach(person -> {
            Employee ep = Pipe.load(person, Employee.class);
            people.add(ep);
        });
        people.sort(Comparator.comparing(Employee::getId));
        return people;
    }


    /**
     * ACCOUNTS
     */
    public static ArrayList<Account> getAccounts() {

        ArrayList<Account> accounts = new ArrayList<>();
        ConnectDB.collection("ACCS").find().forEach(account -> {
            Account ep = Pipe.load(account, Account.class);
            accounts.add(ep);
        });
        accounts.sort(Comparator.comparing(Account::getId));
        return accounts;
    }

    public static Account getAccount(String id) {
        return getAccounts().stream().filter(account -> account.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * AREAS
     */
    public static ArrayList<Area> getAreas() {

        ArrayList<Area> areas = new ArrayList<>();
        ConnectDB.collection("AREAS").find().forEach(area -> {
            Area u = Pipe.load(area, Area.class);
            areas.add(u);
        });
        areas.sort(Comparator.comparing(Area::getId));
        return areas;
    }

    public static List<Area> getAreas(String id) {

        List<Area> areas = new ArrayList<>();
        try (MongoCursor<Document> cur = ConnectDB.collection("AREAS")
                .find(new Document("location", id))
                .iterator()) {
            while (cur.hasNext()) {
                areas.add(Pipe.load(cur.next(), Area.class));
            }
        }
        return areas;
    }

    public static Area getArea(String id) {
        return getAreas().stream().filter(area -> area.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * BINS
     */
    public static List<Bin> getBins(){

        ArrayList<Bin> bins = new ArrayList<>();
        ConnectDB.collection("BNS").find().forEach(bin -> {
            Bin u = Pipe.load(bin, Bin.class);
            bins.add(u);
        });
        bins.sort(Comparator.comparing(Bin::getId));
        return bins;
    }

    public static List<Bin> getBinsForArea(String area) {

        List<Bin> bins = new ArrayList<>();
        try (MongoCursor<Document> cur = ConnectDB.collection("BNS")
                .find(new Document("area", area))
                .iterator()) {
            while (cur.hasNext()) {
                bins.add(Pipe.load(cur.next(), Bin.class));
            }
        }
        return bins;
    }
    public static List<Bin> getBinsForLocation(String location) {

        List<Bin> bins = new ArrayList<>();
        try (MongoCursor<Document> cur = ConnectDB.collection("BNS")
                .find(new Document("location", location))
                .iterator()) {
            while (cur.hasNext()) {
                bins.add(Pipe.load(cur.next(), Bin.class));
            }
        }
        return bins;
    }

    public static Bin getBin(String id) {

        return getBins().stream().filter(bin -> bin.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * EMPLOYEES
     */
    public static ArrayList<Employee> getEmployees() {

        ArrayList<Employee> employees = new ArrayList<>();
        ConnectDB.collection("EMPS").find().forEach(employee -> {
            Employee e = Pipe.load(employee, Employee.class);
            employees.add(e);
        });
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
        ArrayList<Timesheet> timesheets = loadCollection("HR/TMSH", Timesheet.class);
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
        ConnectDB.collection("CATS").find().forEach(catalog -> {
            Catalog u = Pipe.load(catalog, Catalog.class);
            catalogs.add(u);
        });
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
        ConnectDB.collection("USRS").find().forEach(user -> {
            User u = Pipe.load(user, User.class);
            users.add(u);
        });
        return users;
    }

    public static User getUser(String id) {
        return getUsers().stream().filter(u -> u.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * PURCHASE ORDERS
     */
    public static ArrayList<Order> getPurchaseOrders() {

        ArrayList<Order> purchaseOrders = new ArrayList<>();
        ConnectDB.collection("ORDS/PO").find().forEach(purchaseOrder -> {
            Order u = Pipe.load(purchaseOrder, Order.class);
            purchaseOrders.add(u);
        });
        purchaseOrders.sort(Comparator.comparing(Order::getId));
        return purchaseOrders;
    }

    public static Order getPurchaseOrder(String purchaseOrderId) {
        return getPurchaseOrders().stream().filter(purchaseOrder -> purchaseOrder.getOrderId().equals(purchaseOrderId)).toList().stream().findFirst().orElse(null);
    }

    public static List<Order> getPurchaseOrders(String shipTo) {
        return getPurchaseOrders().stream().filter(purchaseOrder -> purchaseOrder.getShipTo().equals(shipTo)).collect(Collectors.toList());
    }

    public static List<Order> getPurchaseOrders(String shipTo, LockeStatus status) {
        return getPurchaseOrders().stream().filter(purchaseOrder -> purchaseOrder.getShipTo().equals(shipTo) && purchaseOrder.getStatus().equals(status)).collect(Collectors.toList());
    }

    /**
     * INVOICES
     */
    public static ArrayList<Order> getInvoices() {

        ArrayList<Order> invoices = new ArrayList<>();
        ConnectDB.collection("INVS").find().forEach(invoice -> {
            Order u = Pipe.load(invoice, Order.class);
            invoices.add(u);
        });
        invoices.sort(Comparator.comparing(Order::getId));
        return invoices;
    }

    public static Order getInvoice(String invoiceId) {
        return getInvoices().stream().filter(invoice -> invoice.getId().equals(invoiceId)).toList().stream().findFirst().orElse(null);
    }

    public static Order getInvoicesForAccount(String accountId) {
        //TODO Fix this to ArrayList
        return getInvoices().stream().filter(invoice -> invoice.getAccount().equals(accountId)).toList().stream().findFirst().orElse(null);
    }

    /**
     * LEDGERS
     */
    public static ArrayList<Ledger> getLedgers() {

        ArrayList<Ledger> ledgers = new ArrayList<>();
        ConnectDB.collection("LGS").find().forEach(ledger -> {
            Ledger u = Pipe.load(ledger, Ledger.class);
            ledgers.add(u);
        });
        ledgers.sort(Comparator.comparing(Ledger::getId));
        return ledgers;
    }

    public static Ledger getLedger(String id) {
        return getLedgers().stream().filter(ledger -> ledger.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static Ledger hasLedger(String locationId) {
        return getLedgers().stream().filter(ledger -> ledger.getLocation().equals(locationId)).toList().stream().findFirst().orElse(null);
    }

    /**
     * RATES
     */
    public static ArrayList<Rate> getRates() {

        ArrayList<Rate> rates = new ArrayList<>();
        ConnectDB.collection("RTS").find().forEach(rate -> {
            Rate r = Pipe.load(rate, Rate.class);
            rates.add(r);
        });
        rates.sort(Comparator.comparing(Rate::getId));
        return rates;
    }

    public static Rate getRate(String id) {
        return getRates().stream().filter(rate -> rate.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * POSITIONS
     */
    public static ArrayList<Position> getPositions() {

        ArrayList<Position> positions = new ArrayList<>();
        ConnectDB.collection("HR/POS").find().forEach(position -> {
            Position u = Pipe.load(position, Position.class);
            positions.add(u);
        });
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
        ConnectDB.collection("TRANS/IDO").find().forEach(inboundDelivery -> {
            Delivery delivery = Pipe.load(inboundDelivery, Delivery.class);
            inboundDeliveries.add(delivery);
        });
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

    public static Delivery getInboundDeliveryForPO(String poNumber) {
        return getInboundDeliveries().stream().filter(delivery -> delivery.getPurchaseOrder().equals(poNumber)).toList().stream().findFirst().orElse(null);
    }

    /**
     * OUTBOUND DELIVERIES
     */
    public static ArrayList<Delivery> getOutboundDeliveries() {

        ArrayList<Delivery> outboundDeliveries = new ArrayList<>();
        ConnectDB.collection("TRANS/ODO").find().forEach(outboundDelivery -> {
            Delivery delivery = Pipe.load(outboundDelivery, Delivery.class);
            outboundDeliveries.add(delivery);
        });
        outboundDeliveries.sort(Comparator.comparing(Delivery::getId));
        return outboundDeliveries;
    }

    public static ArrayList<Delivery> getOutboundDeliveries(String destination) {

        ArrayList<Delivery> outboundDeliveries = new ArrayList<>();
        for(Delivery delivery : getOutboundDeliveries()){
            if(delivery.getOrigin().equals(destination)){
                outboundDeliveries.add(delivery);
            }
        }
        return outboundDeliveries;
    }

    public static Delivery getOutboundDelivery(String id) {
        return getOutboundDeliveries().stream().filter(delivery -> delivery.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * TRUCKS
     */
    public static ArrayList<Truck> getTrucks() {

        ArrayList<Truck> trucks = new ArrayList<>();
        ConnectDB.collection("TRANS/TRCKS").find().forEach(truck -> {
            Truck u = Pipe.load(truck, Truck.class);
            trucks.add(u);
        });
        trucks.sort(Comparator.comparing(Truck::getId));
        return trucks;
    }

    public static Truck getTruck(String id) {
        return getTrucks().stream().filter(truck -> truck.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static Truck getTruckForDelivery(String deliveryId) {
        return getTrucks().stream().filter(truck -> truck.getDelivery().equals(deliveryId)).toList().stream().findFirst().orElse(null);
    }

    /**
     * INVENTORIES
     */
    public static ArrayList<Inventory> getInventories() {

        ArrayList<Inventory> is = new ArrayList<>();
        ConnectDB.collection("STK").find().forEach(stock -> {
            Inventory u = Pipe.load(stock, Inventory.class);
            is.add(u);
        });
        is.sort(Comparator.comparing(Inventory::getLocation));
        return is;
    }

    public static Inventory getInventory(String location) {
        //TODO Test for local disk because I think we need the old way of checking for null and making if so
        return getInventories().stream().filter(stk -> stk.getLocation().equals(location)).toList().stream().findFirst().orElse(null);
    }

    /**
     * RECORDS
     */
    public static ArrayList<ArrayList<Record>> getRecords() {
        ArrayList<ArrayList<Record>> all = new ArrayList<>();
        ConnectDB.collection("RCS").find().forEach(records -> {
            ArrayList<Record> r = new ArrayList<>();
            List<Document> docs = records.getList("records", Document.class);
            if (docs != null) {
                for (Document doc : docs) {
                    r.add(Pipe.load(doc, Record.class));
                }
            }
            all.add(r);
        });

        return all;
    }

    public static void assertRecord(String objex, String id, Record record) {
        String normalized = objex.startsWith("/") ? objex.replaceFirst("/", "") : objex;
        Document filter = new Document("id", id).append("objex", normalized);

        Document existing = ConnectDB.collection("RCS").find(filter).first();
        List<Document> records = new ArrayList<>();
        if (existing != null) {
            List<Document> existingRecords = existing.getList("records", Document.class);
            if (existingRecords != null) {
                records.addAll(existingRecords);
            }
        }

        records.add(Document.parse(gson.toJson(record)));

        Document replacement = new Document("id", id)
                .append("objex", normalized)
                .append("records", records);

        ConnectDB.collection("RCS").replaceOne(filter, replacement, new ReplaceOptions().upsert(true));
    }


    /**
     * ITEMS
     */
    public static ArrayList<Item> getItems() {

        ArrayList<Item> items = new ArrayList<>();
        ConnectDB.collection("ITS").find().forEach(item -> {
            Item u = Pipe.load(item, Item.class);
            items.add(u);
        });
        items.sort(Comparator.comparing(Item::getId));
        return items;
    }

    public static List<Item> getItems(String id) {
        return getItems().stream().filter(item -> item.getOrg().equals(id)).collect(Collectors.toList());
    }

    public static Item getItem(String id) {
        return getItems().stream().filter(i -> i.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    /**
     * BOMS
     */
    public static ArrayList<BillOfMaterials> getBoMs() {

        ArrayList<BillOfMaterials> boms = new ArrayList<>();
        ConnectDB.collection("BOMS").find().forEach(bom -> {
            BillOfMaterials u = Pipe.load(bom, BillOfMaterials.class);
            boms.add(u);
        });
        boms.sort(Comparator.comparing(BillOfMaterials::getId));
        return boms;
    }

    public static List<BillOfMaterials> getBoMs(String finishedItemId) {
        return getBoMs().stream().filter(bom -> bom.getItem().equals(finishedItemId)).collect(Collectors.toList());
    }

    public static BillOfMaterials getBoM(String id) {
        return getBoMs().stream().filter(bom -> bom.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static PurchaseRequisition getPurchaseRequisition(String id) {
        return getPurchaseRequisitions().stream().filter(pr -> pr.getId().equals(id)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<Order> getSalesOrders() {

        ArrayList<Order> salesOrders = new ArrayList<>();
        ConnectDB.collection("ORDS/SO").find().forEach(salesOrder -> {
            Order so = Pipe.load(salesOrder, Order.class);
            salesOrders.add(so);
        });
        salesOrders.sort(Comparator.comparing(Order::getOrderId));
        return salesOrders;
    }

    public static Order getSalesOrder(String salesOrderId) {
        return getSalesOrders().stream().filter(pr -> pr.getId().equals(salesOrderId)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<PurchaseRequisition> getPurchaseRequisitions() {

        ArrayList<PurchaseRequisition> purchaseRequisions = new ArrayList<>();
        ConnectDB.collection("ORDS/PR").find().forEach(purchaseRequisition -> {
            PurchaseRequisition pr = Pipe.load(purchaseRequisition, PurchaseRequisition.class);
            purchaseRequisions.add(pr);
        });
        purchaseRequisions.sort(Comparator.comparing(PurchaseRequisition::getId));
        return purchaseRequisions;
    }

    public static PurchaseRequisition getPurchaseRequisitions(String purchaseRequisitionId) {
        return getPurchaseRequisitions().stream().filter(pr -> pr.getId().equals(purchaseRequisitionId)).toList().stream().findFirst().orElse(null);
    }

    public static ArrayList<GoodsReceipt> getGoodsReceipts() {

        ArrayList<GoodsReceipt> goodsReceipts = new ArrayList<>();
        ConnectDB.collection("GR").find().forEach(goodsReceipt -> {
            GoodsReceipt u = Pipe.load(goodsReceipt, GoodsReceipt.class);
            goodsReceipts.add(u);
        });
        goodsReceipts.sort(Comparator.comparing(GoodsReceipt::getId));
        return goodsReceipts;
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
            case "/ACCS/F" -> {
                return new Finder("/ACCS", Account.class, desktop);
            }
            case "/ACCS/AUTO_MK" -> {
                return new AutoMakeAccounts(desktop, null);
            }
            case "/ACCS/NEW" -> {
                return new CreateAccount(desktop, null);
            }
            case "/ACCS/O" -> {
                String accountId = JOptionPane.showInputDialog("Enter Account ID");
                Account account = Engine.getAccount(accountId);
                return new ViewAccount(account, desktop);
            }

            //AREAS
            case "/AREAS" -> {
                return new Areas(getAreas(), desktop);
            }
            case "/AREAS/F" -> {
                return new Finder("/AREAS", new Area(), desktop);
            }
            case "/AREAS/NEW" -> {
                return new CreateArea(null, desktop, null);
            }
            case "/AREAS/AUTO_MK" -> {
                return new AutoMakeAreas(desktop, null);
            }
            case "/AREAS/MOD" -> {
                String areaId = JOptionPane.showInputDialog(null, "Area ID", "Area ID", JOptionPane.QUESTION_MESSAGE);
                Area area = Engine.getArea(areaId);
                return new ModifyArea(area, desktop, null);
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
                return new Finder("/BNS", new Bin(), desktop);
            }
            case "/BNS/NEW" -> {
                return new CreateBin("", desktop, null);
            }
            case "/BNS/AUTO_MK" -> {
                return new AutoMakeBins(desktop);
            }
            case "/BNS/MOD" -> {
                String binId = JOptionPane.showInputDialog(null, "Enter Bin ID", "Bin ID", JOptionPane.QUESTION_MESSAGE);
                Bin bin = Engine.getBin(binId);
                return new ModifyBin(bin, desktop, null);
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
            case "/ORGS/F" -> {
                return new Finder("/ORGS", new Location(), desktop);
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
            case "/CCS/F" -> {
                return new Finder("/CCS", new Location(), desktop);
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
            case "/CSTS/F" -> {
                return new Finder("/CSTS", new Location(), desktop);
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
            case "/DCSS/F" -> {
                return new Finder("/DCSS", new Location(), desktop);
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
            case "/TRANS/CRRS/F" -> {
                return new Finder("/TRANS/CRRS", new Location(), desktop);
            }
            case "/TRANS/CRRS/NEW" -> {
                return new CreateLocation("/TRANS/CRRS", desktop, null);
            }

            //OUTBOUND DELIVERIES
            case "/TRANS/ODO" -> {
                return new OutboundDeliveries(desktop);
            }
            case "/TRANS/ODO/F" -> {
                return new Finder("/TRANS/ODO", new Delivery(), desktop);
            }
            case "/TRANS/ODO/NEW" -> {
                return new CreateOutboundDeliveryOrder();
            }
            case "/TRANS/IDO" -> {
                return new InboundDeliveries(desktop);
            }
            case "/TRANS/IDO/F" -> {
                return new Finder("/TRANS/IDO", new Delivery(), desktop);
            }
            case "/TRANS/IDO/NEW" -> {
                return new CreateInboundDeliveryOrder();
            }

            //TRUCKS
            case "/TRANS/TRCKS" -> {
                return new Trucks(desktop);
            }
            case "/TRANS/TRCKS/F" -> {
                return new Finder("/TRANS/TRCKS", new Delivery(), desktop);
            }
            case "/TRANS/TRCKS/NEW" -> {
                return new CreateTruck(desktop);
            }

            //WAREHOUSES
            case "/WHS" -> {
                return new Locations("/WHS", desktop);
            }
            case "/WHS/F" -> {
                return new Finder("/WHS", new Location(), desktop);
            }
            case "/WHS/NEW" -> {
                return new CreateLocation("/WHS", desktop, null);
            }

            //VENDORS
            case "/VEND" -> {
                return new Locations("/VEND", desktop);
            }
            case "/VEND/F" -> {
                return new Finder("/VEND", new Location(), desktop);
            }
            case "/VEND/NEW" -> {
                return new CreateLocation("/VEND", desktop, null);
            }

            //RATES
            case "/RTS" -> {
                return new Rates(desktop);
            }
            case "/RTS/F" -> {
                return new Finder("/RTS", new Rate(), desktop);
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
            case "/LGS/F" -> {
                return new Finder("/LGS", new Ledger(), desktop);
            }
            case "/LGS/NEW" -> {
                return new CreateLedger(desktop);
            }
            case "/LGS/AUTO_MK" -> {
                return new AutoMakeLedgers(desktop, null);
            }
            case "/LGS/DEL" -> {
//                return new CreateLedger(desktop);
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
            case "/EMPS/F" -> {
                return new Finder("/EMPS", new Employee(), desktop);
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
            case "/PPL/F" -> {
                return new Finder("/PPL", new Employee(), desktop);
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
            case "/DPTS/F" -> {
                return new Finder("/DPTS", new Department(), desktop);
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
            case "/USRS/F" -> {
                return new Finder("/USRS", new User(), desktop);
            }
            case "/USRS/O" -> {
                String uId = JOptionPane.showInputDialog(null, "Enter User ID", "User ID", JOptionPane.QUESTION_MESSAGE);
                User user = Engine.getUser(uId);
                return new ViewUser(desktop, user);
            }
            case "/USRS/CHG_PSSWD" -> {
                return new ChangeUserPassword();
            }
            case "/USRS/MOD/ALKS" -> {
                return new AddLocke();
            }
            case "/USRS/NEW" -> {
                return new CreateUser(desktop, null);
            }

            //STOCK AND INVENTORY
            case "/STK" -> {
                return new ViewInventory(desktop, Engine.getOrganization().getId());
            }
            case "/STK/MOD/MV" -> {
                return new MoveStock("", null);
            }
            case "/STK/MV/BN" -> {
                return new MoveToBin(null);
            }
            case "/STK/MV/BB" -> {
                return new MoveBinToBin(null);
            }
            case "/STK/MV/FULL" -> {
                return new MoveToBinFull(null);
            }
            case "/STK/MV/DFULL" -> {
                return new DirectedMoveToBinFull(null);
            }

            //INVOICES
            case "/INVS" -> {
                return new Invoices(desktop);
            }
            case "/INVS/NEW" -> {
                return new CreateInvoice(desktop);
            }

            //CATALOGS
            case "/CATS" -> {
                return new Catalogs(desktop);
            }
            case "/CATS/F" -> {
                return new Finder("/CATS", new Catalog(), desktop);
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
                return new Items(getItems(), desktop);
            }
            case "/ITS/F" -> {
                return new Finder("/ITS", new Item(), desktop);
            }
            case "/ITS/NEW" -> {
                return new CreateItem(desktop, null);
            }
            case "/ITS/MOD" -> {
                String eid = JOptionPane.showInputDialog(null, "Enter Item ID", "Item ID", JOptionPane.QUESTION_MESSAGE);
                Item i = Engine.getItem(eid);
                return new ModifyItem(i, desktop, null);
            }
            case "/ITS/O" -> {
                String eid = JOptionPane.showInputDialog(null, "Enter Item ID", "Item ID", JOptionPane.QUESTION_MESSAGE);
                Item i = Engine.getItem(eid);
                return new ViewItem(i, desktop, null);
            }

            //BILL OF MATERIALS
            case "/BOMS" -> {
                return new BOMs(getBoMs(), desktop);
            }
            case "/BOMS/F" -> {
                return new Finder("/BOMS", BillOfMaterials.class, desktop);
            }
            case "/BOMS/NEW" -> {
                return new CreateBOM(desktop, null);
            }
            case "/BOMS/O" -> {
                String bomId = JOptionPane.showInputDialog("Bill of Materials ID");
                BillOfMaterials bom = getBoM(bomId);
                return new ViewBOM(bom, desktop, null);
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
                Order purchaseOrder = Engine.getPurchaseOrder(poId);
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
                return new CreatePurchaseRequisition(desktop);
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
                return new CreateSalesOrder(desktop);
            }
            case "/ORDS/SO/O" -> {
                String soId = JOptionPane.showInputDialog(null, "Enter Sales Order ID", "Sales Order ID", JOptionPane.QUESTION_MESSAGE);
                Order salesOrder = Engine.getSalesOrder(soId);
                return new ViewSalesOrder(salesOrder, desktop, null);
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
                return new CreateTask(desktop, null);
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
        if (transactionCode.endsWith("/MOD")) {
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
                Location ccs = Engine.getLocation(oid, "CCS");
                return new ViewLocation(ccs, desktop);
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
                for (Order l : getPurchaseOrders()) {
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

        if(transactionCode.contains("/MOD/")) {

            String t2 = chs[1] + "/MOD";
            oid = chs[3];
            switch (t2) {
                case "ORGS/MOD" -> {
                    for (Location org : Engine.getLocations("ORGS")) {
                        if (org.getId().equals(oid)) {
                            return new ModifyLocation(org, null);
                        }
                    }
                }
                case "AREAS/MOD" -> {
                    for (Area area : Engine.getAreas()) {
                        if (area.getId().equals(oid)) {
                            return new ModifyArea(area, desktop, null);
                        }
                    }
                }
                case "BNS/MOD" -> {
                    Bin bin = Engine.getBin(oid);
                    if (bin != null) {
                        return new ModifyBin(bin, desktop, null);
                    }
                }
                case "CCS/MOD" -> {
                    for (Location costcenter : Engine.getLocations("CCS")) {
                        if (costcenter.getId().equals(oid)) {
                            return new ModifyLocation(costcenter, null);
                        }
                    }
                }
                case "CSTS/MOD" -> {
                    for (Location l : Engine.getLocations("CSTS")) {
                        if (l.getId().equals(oid)) {
                            return new ModifyLocation(l, null);
                        }
                    }
                }
                case "DCSS/MOD" -> {
                    for (Location l : Engine.getLocations("DCSS")) {
                        if (l.getId().equals(oid)) {
                            return new ModifyLocation(l, null);
                        }
                    }
                }
                case "VEND/MOD" -> {
                    for (Location l : Engine.getLocations("VEND")) {
                        if (l.getId().equals(oid)) {
                            return new ModifyLocation(l, null);
                        }
                    }
                }
                case "ITS/MOD" -> {
                    Item i = Engine.getItem(oid);
                    if (i != null) {
                        return new ModifyItem(i, desktop, null);
                    }
                }
                case "USRS/MOD" -> {
                    User u = Engine.getUser(oid);
                    if (u != null) {
                    }
                }
                case "EMPS/MOD" -> {
                    for (Employee employee : Engine.getEmployees()) {
                        if (employee.getId().equals(oid)) {
                            return new ModifyEmployee(employee, desktop, null);
                        }
                    }
                }
                case "LGS/MOD" -> {
                    for (Ledger ledger : getLedgers()) {
                        if (ledger.getId().equals(oid)) {
                            return new ViewLedger(ledger, desktop);
                        }
                    }
                    return new Ledgers(desktop);
                }
                case "WHS/MOD" -> {
                    for (Location l : getLocations("WHS")) {
                        if (l.getId().equals(oid)) {
                            return new ModifyLocation(l, null);
                        }
                    }
                    return new Locations("/WHS", desktop);
                }
                case "ORDS/PO/MOD" -> {
                    for (Order l : getPurchaseOrders()) {
                        if (l.getOrderId().equals(oid)) {
                            return new ViewPurchaseOrder(l, desktop, null);
                        }
                    }
                    return new PurchaseOrders(desktop);
                }
                case "CATS/MOD" -> {
                    return new Catalogs(desktop);
                }
            }
        }
        return null;
    }
}