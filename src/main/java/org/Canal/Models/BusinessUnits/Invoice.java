package org.Canal.Models.BusinessUnits;

import org.Canal.Models.SupplyChainUnits.Flex;
import java.util.HashMap;

public class Invoice {

    private int date;
    private String id;
    private String number;
    private String billTo;
    public String soldTo;
    private HashMap<Flex, Integer> items;
}