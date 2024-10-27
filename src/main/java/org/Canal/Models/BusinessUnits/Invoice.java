package org.Canal.Models.BusinessUnits;

import org.Canal.Models.SupplyChainUnits.Flex;

import java.util.HashMap;

public class Invoice {

    private int date, number;
    private String billTo, soldTo;
    private HashMap<Flex, Integer> items;
}