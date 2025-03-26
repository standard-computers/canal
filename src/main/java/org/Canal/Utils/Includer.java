package org.Canal.Utils;

import org.Canal.Models.SupplyChainUnits.Item;

public interface Includer {
    void commitInclusion(Item item, double qty, String uom);
    void commitUoM(String uom, String qty, String baseQtyUom);
    void commitVariant(String id, String price);
}