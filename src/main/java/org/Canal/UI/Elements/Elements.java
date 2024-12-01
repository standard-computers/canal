package org.Canal.UI.Elements;

import com.formdev.flatlaf.ui.FlatBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Elements {

    public static JTextField input() {
        JTextField textField = new JTextField();
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JTextField input(String preset) {
        JTextField textField = new JTextField(preset);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JTextField input(int length) {
        JTextField textField = new JTextField(length);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JTextField input(String preset, int length) {
        JTextField textField = new JTextField(preset, length);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 12));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JLabel label(String text){
        JLabel l = new JLabel(text);
        l.setBorder(new EmptyBorder(0, 10, 10, 0));
        return l;
    }

    public static JLabel h2(String text){
        JLabel l = new JLabel(text);
        l.setFont(UIManager.getFont("h2.font"));
        l.setBorder(new EmptyBorder(5, 5, 5, 5));
        return l;
    }

    public static JLabel h3(String text){
        JLabel l = new JLabel(text);
        l.setFont(UIManager.getFont("h3.font"));
        l.setBorder(new EmptyBorder(5, 5, 5, 5));
        return l;
    }

    public static JLabel h3(String text, Color color){
        JLabel l = new JLabel(text);
        l.setFont(UIManager.getFont("h3.font"));
        l.setForeground(color);
        l.setBorder(new EmptyBorder(5, 5, 5, 5));
        return l;
    }

    public static JLabel link(String text, String tooltip) {
        JLabel link = new JLabel(text);
        if (tooltip != null && !tooltip.isEmpty()) {
            link.setToolTipText(tooltip);
        }
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Font originalFont = link.getFont();
        Font underlineFont = originalFont.deriveFont(Font.PLAIN);
        link.setFont(underlineFont);
        Border paddedDashedBottomBorder = BorderFactory.createCompoundBorder(
                new DashedBottomBorder(2, 4),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)
        );
        link.setBorder(paddedDashedBottomBorder);
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                link.setForeground(link.getForeground().darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                link.setForeground(link.getForeground().brighter());
            }
        });
        return link;
    }

    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(190, 35));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Reset cursor when not hovering
            }
        });
        return b;
    }

    /**
     * Center GUI header for Windows
     * @param text Header Text
     * @return JPanel
     */
    public static JPanel header(String text){
        JPanel panel = new JPanel();
        panel.add(Elements.h2(text));
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.setBackground(UIManager.getColor("Component.selectionBackground"));
        return panel;
    }

    public static JPanel header(String text, int alignment) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = Elements.h2(text);
        label.setHorizontalAlignment(alignment);
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.setBackground(UIManager.getColor("Component.selectionBackground"));
        return panel;
    }
}