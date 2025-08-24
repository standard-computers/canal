package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;
import org.Canal.Models.SupplyChainUnits.MaterialMovement;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Start;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.LockeStatus;

import java.io.File;
import java.util.ArrayList;

public class Inventory extends Objex {

    private String location;
    private ArrayList<StockLine> stockLines = new ArrayList<StockLine>();
    private ArrayList<MaterialMovement> materialMovements = new ArrayList<>();

    public Inventory(String location) {
        this.location = location;
    }

    public void addStock(StockLine stockLine) {
        stockLines.add(stockLine);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<StockLine> getStockLines() {
        return stockLines;
    }

    public void setStockLines(ArrayList<StockLine> stockLines) {
        this.stockLines = stockLines;
    }

    public void removeStock(StockLine stockLine) {
        stockLines.remove(stockLine);
    }

    public ArrayList<MaterialMovement> getMaterialMovements() {
        return materialMovements;
    }

    public void setMaterialMovements(ArrayList<MaterialMovement> materialMovements) {
        this.materialMovements = materialMovements;
    }

    public void addMaterialMovement(MaterialMovement materialMovement) {
        this.materialMovements.add(materialMovement);
    }

    public boolean inStock(String itemId) {
        for (StockLine stockLine : stockLines) {
            if (stockLine.getId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    public double getStock(String itemId) {
        for (StockLine stockLine : stockLines) {
            if (stockLine.getId().equals(itemId)) {
                return stockLine.getQuantity();
            }
        }
        return 0.0;
    }

    public StockLine getStockLine(String itemId) {
        for (StockLine stockLine : stockLines) {
            if (stockLine.getId().equals(itemId)) {
                return stockLine;
            }
        }
        return null;
    }

    public void goodsIssue(StockLine stockLine, double quantity) {
        for (StockLine sl : stockLines) {
            if (sl.equals(stockLine)) { // Assuming equals() is properly overridden
                stockLine.setQuantity(stockLine.getQuantity() - quantity);
                if (sl.getQuantity() == 0) {
                    stockLines.remove(sl);
                }
                MaterialMovement nmm = new MaterialMovement();
                nmm.setObjex(stockLine.getObjex());
                nmm.setUser(Engine.getAssignedUser().getId());
                nmm.setSourceHu(stockLine.getHu());
                nmm.setDestinationBin("OUTBOUND");
                nmm.setDestinationHu("NO_HU");
                nmm.setType("Goods Issue");
                nmm.setStatus(LockeStatus.COMPLETED);
                materialMovements.add(nmm);
//                if (stockLine.getQuantity() >= quantity) {
//                } else {
//                    System.out.println("Not enough stock to issue the requested quantity.");
//                }
                save();
                return;
            }
        }
        System.out.println("Stock line not found.");
    }

    public void save() {
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File md = new File(Start.DIR + "\\.store\\STK\\");
            File[] mdf = md.listFiles();
            if (mdf != null) {
                for (File file : mdf) {
                    if (file.getPath().endsWith(".stk")) {
                        Inventory forg = Pipe.load(file.getPath(), Inventory.class);
                        if (forg.getLocation().equals(location)) {
                            Pipe.export(file.getPath(), this);
                        }
                    }
                }
            }
        } else {
            Pipe.save("STK", this);
        }
    }
}