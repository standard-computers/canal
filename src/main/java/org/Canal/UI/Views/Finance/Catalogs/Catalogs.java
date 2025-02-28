package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /CATS
 */
public class Catalogs extends LockeState {

    private DesktopState desktop;
    private CustomTable table;

    public Catalogs(DesktopState desktop) {

        super("Catalogs", "/CATS", false, true, false, true);
        setFrameIcon(new ImageIcon(Catalogs.class.getResource("/icons/catalogs.png")));
        this.desktop = desktop;

        setLayout(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(new ViewCatalog(Engine.getCatalog(value)));
                    }
                }
            }
        });
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Name",
                "Description",
                "Period",
                "Valid From",
                "Valid To",
                "Avl CCS",
                "Avl CSTS",
                "Avl VEND",
                "Items",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Catalog catalog : Engine.getCatalogs()) {
            data.add(new Object[]{
                    catalog.getId(),
                    catalog.getName(),
                    catalog.getDescription(),
                    catalog.getPeriod(),
                    catalog.getValidFrom(),
                    catalog.getValidTo(),
                    catalog.getCostCenters().size(),
                    catalog.getCustomers().size(),
                    catalog.getVendors().size(),
                    catalog.getItems().size(),
                    catalog.getStatus(),
                    catalog.getCreated(),
            });
        }
        return new CustomTable(columns, data);
    }
}