package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
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
        main.addTab("Configuration", codex());
        main.addTab("CODEX", codex());
        main.addTab("Locations", locations());
        main.addTab("/ORDS", orders());
        main.addTab("/LGS", ledgers());
        main.addTab("/AREAS", ledgers());
        main.addTab("/ITS", ledgers());
        main.addTab("/INV", invoices());
        main.addTab("/USRS", invoices());
        main.addTab("/EMPS", invoices());
        main.addTab("/PPL", invoices());
        main.addTab("/X", invoices());
        main.addTab("/RCS", invoices());
        setLayout(new BorderLayout());
        add(Elements.header("Data Center", SwingConstants.LEFT), BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        setMaximized(true);
    }

    private CustomTabbedPane codex() {
        CustomTabbedPane codex = new CustomTabbedPane();

        return codex;
    }

    private CustomTabbedPane locations() {
        CustomTabbedPane locations = new CustomTabbedPane();
        locations.addTab("/ORGS", null);
        locations.addTab("/CCS", null);
        locations.addTab("/DCSS", null);
        locations.addTab("/WHS", null);
        locations.addTab("/VEND", null);
        locations.addTab("/CSTS", null);
        return locations;
    }

    private CustomTabbedPane orders() {
        CustomTabbedPane locations = new CustomTabbedPane();
        locations.addTab("/PR", null);
        locations.addTab("/PO", null);
        locations.addTab("/SO", null);
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