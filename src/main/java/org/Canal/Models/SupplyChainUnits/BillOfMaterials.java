package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import java.io.File;
import java.util.ArrayList;

public class BillOfMaterials extends Objex {

    private String location; //If not empty, BOM valid only at this location
    private String item; //Finished Item ID
    private String customer; //Used if customer assigned to manuracturing order
    private ArrayList<StockLine> components;
    private ArrayList<Task> steps;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ArrayList<StockLine> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<StockLine> components) {
        this.components = components;
    }

    public ArrayList<Task> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Task> steps) {
        this.steps = steps;
    }

    public void save() {
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File md = new File(Start.DIR + "\\.store\\BOMS\\");
            File[] mdf = md.listFiles();
            if (mdf != null) {
                for (File file : mdf) {
                    if (file.getPath().endsWith(".boms")) {
                        BillOfMaterials fl = Pipe.load(file.getPath(), BillOfMaterials.class);
                        if (fl.getId().equals(id)) {
                            Pipe.export(file.getPath(), this);
                        }
                    }
                }
            }
        } else {
            Pipe.save("BOMS", this);
        }
    }
}
