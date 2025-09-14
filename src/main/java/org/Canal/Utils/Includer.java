package org.Canal.Utils;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Task;

public interface Includer {
    void commitInclusion(Item item, double qty, String uom);
    void commitUoM(String uom, double qty, String baseQtyUom);
    void commitStep(Task task);
}