package org.Canal.Models.BusinessUnits;

import java.util.HashMap;

public class Stock {

    private HashMap<String, Integer> regularStock;
    private HashMap<String, Integer> consumableStock;

    private class StockLine {

        private String objex_id; //ITS, MTS, or CMPS ID
        private double quantity;
        private String area;
        private String bin;


    }
}