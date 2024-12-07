package org.Canal.UI.Views.Orders.SalesOrders;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import java.util.HashMap;

/**
 * /ORDS/SO/NEW
 */
public class CreateSalesOrder extends LockeState {

    public CreateSalesOrder(){
        super("Create Sales Order", "/ORDS/SO/NEW", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setTitle("Create Sales Order");
        HashMap<String, String> opts = new HashMap<>();
        for(Location cs : Engine.getCustomers()){
            opts.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Vendor vs : Engine.getVendors()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        Selectable ats = new Selectable(opts);
    }
}