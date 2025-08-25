package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;
import org.Canal.Models.SupplyChainUnits.StockLine;

import java.util.ArrayList;

public class Invoice extends Objex {

    private int date;
    private String number;
    private String billTo;
    public String soldTo;
    private ArrayList<StockLine> lineitems;

}