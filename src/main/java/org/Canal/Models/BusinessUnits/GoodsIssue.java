package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;
import org.Canal.Models.SupplyChainUnits.StockLine;

import java.util.ArrayList;

public class GoodsIssue extends Objex {

    private String comments;
    private String issuer; //User ID of owner
    public String location; //Location receiving in at
    public ArrayList<StockLine> items; //ITS received and qty received

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<StockLine> getItems() {
        return items;
    }

    public void setItems(ArrayList<StockLine> items) {
        this.items = items;
    }
}