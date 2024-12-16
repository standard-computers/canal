package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Utils.LockeStatus;

import java.util.ArrayList;

public class Delivery extends Objex {

    private String salesOrder;
    private String purchaseOrder; //PO delivery is a result of
    private String expectedDelivery; //When it should arrive, could differ from PO
    private String destination; //Location ID
    private Area destinationArea; //
    private Bin destinationDoor; //
    private LockeStatus status; //Status of this Delivery
    private Truck truck;
    private ArrayList<StockLine> pallets;


}