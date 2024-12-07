package org.Canal.UI.Views.Inventory;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Engine;

import java.util.HashMap;

public class CreateSTO extends LockeState {

    public CreateSTO(){
        super("Create Stock Transfer Order", "/", false, true, false, true);
        HashMap<String, String> opts = new HashMap<>();
        for(Location cs : Engine.getCustomers()){
            opts.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Vendor vs : Engine.getVendors()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        Selectable senderOps = new Selectable(opts);
        Selectable destinationOps = new Selectable(opts);
    }
}