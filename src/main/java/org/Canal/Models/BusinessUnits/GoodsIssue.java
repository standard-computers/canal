package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;
import org.Canal.Utils.LockeStatus;

import java.util.ArrayList;

public class GoodsIssue extends Objex {

    private String comments;
    private String issuer; //User ID of owner
    public String location; //Location receiving in at
    public ArrayList<OrderLineItem> items; //ITS received and qty received

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

    public ArrayList<OrderLineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderLineItem> items) {
        this.items = items;
    }
}