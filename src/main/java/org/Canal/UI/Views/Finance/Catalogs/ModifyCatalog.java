package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CATS/MOD
 * Modify a Catalog with Catalog ID.
 * This is more of a Catalog Editor with rich features like the builders.
 */
public class ModifyCatalog extends LockeState {

    private Catalog catalog;

    public ModifyCatalog(Catalog catalog) {
        super("Catalog Modifier", "/CATS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyCatalog.class.getResource("/icons/modify.png")));
        this.catalog = catalog;
        JTable table = createTable();
        table.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        setLayout(new BorderLayout());
        add(Elements.header("/CATS OBJEX EDITOR", SwingConstants.LEFT), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
        JButton save = Elements.button("Save");
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