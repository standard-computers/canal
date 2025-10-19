package org.Canal.UI.Views.Trucks;

import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.RefreshListener;

public class ViewTruck extends LockeState {

    private Truck truck;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewTruck(Truck truck, DesktopState desktop, RefreshListener refreshListener) {

        super("Truck " + truck.getId() + " / " + truck.getNumber(), "/TRANS/TRCKS/" + truck.getId());

    }
}
