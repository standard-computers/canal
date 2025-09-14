package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import java.io.File;

public class Truck extends Objex {

    private String number = "N/A"; //Actual Truck Number
    private String carrier; //Carrier ID
    private String driver = ""; //Person ID
    private String year = "";
    private String make = "";
    private String model = "";
    private String delivery = ""; //Delivery ID (IDO or ODO)

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void save() {
        if (Engine.getConfiguration().getMongodb().isEmpty()) {

            File md = new File(Start.DIR + "\\.store\\TRANS\\TRCKS\\");
            File[] mdf = md.listFiles();
            if (mdf != null) {
                for (File file : mdf) {
                    if (file.getPath().endsWith(".trans.trcks")) {
                        BillOfMaterials fl = Pipe.load(file.getPath(), BillOfMaterials.class);
                        if (fl.getId().equals(id)) {
                            Pipe.export(file.getPath(), this);
                        }
                    }
                }
            }
        } else {
            Pipe.save("TRANS/TRCKS", this);
        }
    }
}