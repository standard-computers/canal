package org.Canal.UI.Elements;

import org.Canal.UI.ColorUtil;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTabbedPane extends JTabbedPane {

    public CustomTabbedPane() {

        setUI(new FlatLafCompatibleTabbedPaneUI());
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = getUI().tabForCoordinate(CustomTabbedPane.this, e.getX(), e.getY());
                if (index >= 0) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    private static class FlatLafCompatibleTabbedPaneUI extends BasicTabbedPaneUI {

        @Override
        protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
            Graphics2D g2d = (Graphics2D) g.create();
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Rectangle tabRect = rects[tabIndex];
            Polygon tabShape = new Polygon();
            tabShape.addPoint(tabRect.x, tabRect.y + tabRect.height);
            tabShape.addPoint(tabRect.x, tabRect.y);
            tabShape.addPoint(tabRect.x + tabRect.width - 13, tabRect.y);
            tabShape.addPoint(tabRect.x + tabRect.width, tabRect.y + 13);
            tabShape.addPoint(tabRect.x + tabRect.width, tabRect.y + tabRect.height);
            Color backgroundColor = tabPane.getSelectedIndex() == tabIndex
                    ? ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.85f)
                    : UIManager.getColor("Panel.background");
            if (backgroundColor == null) {
                backgroundColor = tabPane.getSelectedIndex() == tabIndex ? Color.LIGHT_GRAY : Color.WHITE;
            }
            g2d.setColor(backgroundColor);
            g2d.fillPolygon(tabShape);
            g2d.setColor(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.85f));
            g2d.drawPolygon(tabShape);
            Font font = new Font(
                    UIManager.getFont("Label.font").getName(),
                    Font.PLAIN,
                    Engine.getConfiguration().getFontSize() + 1
            );
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            String title = tabPane.getTitleAt(tabIndex);
            Icon icon = tabPane.getIconAt(tabIndex);
            int textX = tabRect.x + 10;
            int textY = tabRect.y + (tabRect.height + metrics.getAscent() - metrics.getDescent()) / 2;
            if (icon != null) {
                icon.paintIcon(tabPane, g, tabRect.x + 5, tabRect.y + (tabRect.height - icon.getIconHeight()) / 2);
                textX += icon.getIconWidth() + 5;
            }
            if (title != null) {
                g.setColor(UIManager.getColor("Label.foreground"));
                g.drawString(title, textX, textY);
            }
            g2d.dispose();
        }
    }
}