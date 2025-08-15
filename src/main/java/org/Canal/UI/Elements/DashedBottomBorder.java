package org.Canal.UI.Elements;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class DashedBottomBorder extends AbstractBorder {
    private final int dashLength;
    private final int padding;

    public DashedBottomBorder(int dashLength, int padding) {
        this.dashLength = dashLength;
        this.padding = padding;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(c.getForeground());
        float[] dashPattern = {dashLength};
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashPattern, 0));
        g2.drawLine(x + padding, y + height - 1, x + width - padding, y + height - 1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, padding, 1, padding);
    }
}