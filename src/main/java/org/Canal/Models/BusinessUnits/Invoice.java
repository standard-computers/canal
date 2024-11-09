package org.Canal.Models.BusinessUnits;

import java.util.HashMap;

public class Invoice {

    private int date;
    private String id;
    private String number;
    private String billTo;
    public String soldTo;
    private HashMap<OrderLineItem, Integer> items;
}