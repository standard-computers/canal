package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CATS/MOD
 */
public class ModifyCatalog extends JInternalFrame {

    private Catalog catalog;

    public ModifyCatalog(Catalog catalog) {
        super("Catalog Modifier", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyCatalog.class.getResource("/icons/modify.png")));
        this.catalog = catalog;
        JTable table = createTable();
        table.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        setLayout(new BorderLayout());
        add(Elements.header("/CATS OBJEX EDITOR", SwingConstants.LEFT), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
        org.Canal.UI.Elements.Button save = new Button("Save");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        add(save, BorderLayout.SOUTH);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW));
    }

    private JTable createTable() {
        String[] columns = new String[]{"Property", "Value"};
        String[][] data = {
                {"Id", "catalog"},
                {"Name", "catalog"},
                {"Cost Centers", "catalog"},
                {"Customers", "catalog"},
                {"vendors", "catalog"},
        };
        JTable table = new JTable(data, columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
    }
}