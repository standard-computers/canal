package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

public class Invoice extends Objex {

    private int date;
    private String number;
    private String purchaseRequisition;
    private String billTo;
    private String soldTo;
    private String expectedDelivery;
    private String paymentDue;
    private ArrayList<OrderLineItem> items = new ArrayList<>();
    private ArrayList<Rate> rates = new ArrayList<>();
    private double netValue;
    private double taxAmount;
    private double total;

}