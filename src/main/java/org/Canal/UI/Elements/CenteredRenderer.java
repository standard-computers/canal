package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CenteredRenderer extends DefaultTableCellRenderer {
    public CenteredRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER); // Center text
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
        return c;
    }
}
