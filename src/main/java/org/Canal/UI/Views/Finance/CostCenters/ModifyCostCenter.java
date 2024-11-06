package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CCS/MOD
 */
public class ModifyCostCenter extends JInternalFrame {

    private Location costCenter;

    public ModifyCostCenter(Location costCenter) {
        super(costCenter.getId(), false, true, false, true);
        this.costCenter = costCenter;
        JTable table = createTable();
        table.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        setLayout(new BorderLayout());
        add(Elements.header("/CCS OBJEX EDITOR", SwingConstants.LEFT), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
        Button save = new Button("Save");
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
                {"Id", costCenter.getId()},
                {"Name", costCenter.getName()},
                {"Street", costCenter.getLine1()},
                {"City", costCenter.getCity()},
                {"State", costCenter.getState()},
                {"Postal", costCenter.getPostal()},
                {"Country", costCenter.getCountry()},
                {"Tax Exempt", String.valueOf(costCenter.isTaxExempt())}
        };
        JTable table = new JTable(data, columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
    }
}