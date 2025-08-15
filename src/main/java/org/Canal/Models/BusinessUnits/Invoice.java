package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.HashMap;

public class Invoice extends Objex {

    private int date;
    private String number;
    private String billTo;
    public String soldTo;
    private HashMap<OrderLineItem, Integer> items;

}