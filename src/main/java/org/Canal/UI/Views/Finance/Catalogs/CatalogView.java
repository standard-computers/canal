package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CATS/$[CATALOG_ID]
 */
public class CatalogView extends LockeState {

    private Catalog catalog;
    private int currentPage = 1;
    private JTextField pageIndicator;

    public CatalogView(Catalog catalog) {

        super("Catalog", "/CATS/$", true, true, true, true);
        setFrameIcon(new ImageIcon(CatalogView.class.getResource("/icons/catalogs.png")));
        this.catalog = catalog;

        setLayout(new BorderLayout());
        add(controls(), BorderLayout.NORTH);
    }

    private JPanel controls() {

        JPanel panel = new JPanel();
        JLabel l = Elements.label(catalog.getName());
        pageIndicator = Elements.input(String.valueOf(currentPage), 3);
        JButton addPage = Elements.button("Add Page");
        JButton nextPage = Elements.button("<");
        JButton previousPage = Elements.button(">");

        panel.add(l);
        panel.add(pageIndicator);
        panel.add(addPage);
        panel.add(nextPage);
        panel.add(previousPage);

        addPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        nextPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        previousPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        return panel;
    }
}