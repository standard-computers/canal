package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.ViewLocation;

import javax.swing.*;
import java.awt.*;

/**
 * /DATA_CNTR
 * This is a holistic object (JSON) editor for master
 * data management.
 */
public class DataCenter extends LockeState {

    public DataCenter() {
        super("Data Center", "/DATA_CNTR", true, true, true, true);
        setFrameIcon(new ImageIcon(ViewLocation.class.getResource("/icons/datacenter.png")));
        super.isMaximized();
        CustomTabbedPane main = new CustomTabbedPane();
        main.addTab("CODEX", codex());
        main.addTab("Locations", locations());
        main.addTab("Stock", stock());
        main.addTab("/LGS", ledgers());
        main.addTab("/INV", invoices());
        setLayout(new BorderLayout());
        add(main, BorderLayout.CENTER);
    }

    private CustomTabbedPane codex() {
        CustomTabbedPane codex = new CustomTabbedPane();

        return codex;
    }

    private CustomTabbedPane locations() {
        CustomTabbedPane locations = new CustomTabbedPane();
        locations.addTab("/ORGS (Orgs)", null);
        locations.addTab("/CCS (Cost Centers)", null);
        locations.addTab("/DCSS (Dist Centers)", null);
        locations.addTab("/WHS (Warehouses)", null);
        locations.addTab("/VEND (Vendors)", null);
        locations.addTab("/CSTS (Customers)", null);
        return locations;
    }

    private CustomTabbedPane stock() {
        CustomTabbedPane stock = new CustomTabbedPane();

        return stock;
    }

    private CustomTabbedPane ledgers() {
        CustomTabbedPane ledgers = new CustomTabbedPane();

        return ledgers;
    }

    private CustomTabbedPane invoices() {
        CustomTabbedPane invoices = new CustomTabbedPane();

        return invoices;
    }
}