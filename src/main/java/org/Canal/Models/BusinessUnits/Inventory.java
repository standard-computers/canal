package org.Canal.Models.BusinessUnits;

import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Start;
import org.Canal.Utils.Json;

import java.io.File;
import java.util.ArrayList;

public class Inventory {

    private String location;
    private ArrayList<StockLine> stockLines =  new ArrayList<StockLine>();

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

    public void save(){
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\STK\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".stk")) {
                    Inventory forg = Json.load(file.getPath(), Inventory.class);
                    if (forg.getLocation().equals(location)) {
                        Json.save(file.getPath(), this);
                    }
                }
            }
        }
    }
}