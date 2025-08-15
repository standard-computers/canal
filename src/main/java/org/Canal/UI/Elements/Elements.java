package org.Canal.UI.Elements;

import com.formdev.flatlaf.ui.FlatBorder;
import org.Canal.UI.ColorUtil;
import org.Canal.Utils.Engine;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Elements {

    public static JTextField input() {
        JTextField textField = new JTextField();
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JTextField input(String preset) {
        JTextField textField = new JTextField(preset);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JTextField input(int length) {
        JTextField textField = new JTextField(length);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JTextField input(String preset, int length) {
        JTextField textField = new JTextField(preset, length);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        return textField;
    }

    public static JLabel label(String text){
        JLabel l = new JLabel(text);
        l.setBorder(new EmptyBorder(0, 10, 10, 0));
        return l;
    }

    public static JLabel coloredLabel(String text, Color color){
        JLabel l = new JLabel(text);
        l.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        l.setMinimumSize(new Dimension(120, 25));
        l.setMaximumSize(new Dimension(200, 25));
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 2, 1, 0, color),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
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
        b.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(190, 35));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
        panel.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.8f)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return panel;
    }

    public static RTextScrollPane simpleEditor(){
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        textArea.setCodeFoldingEnabled(true);
        textArea.setLineWrap(false);
        return new RTextScrollPane(textArea);
    }

    public static JTextField selector(String preset, HashMap<String, String> options, JInternalFrame parentFrame) {
        JTextField textField = input(preset);
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(300, textField.getPreferredSize().height));
        container.add(textField, BorderLayout.CENTER);
        JButton selectorButton = new JButton("...");
        selectorButton.setPreferredSize(new Dimension(30, textField.getPreferredSize().height));
        selectorButton.setVisible(true);
        container.add(selectorButton, BorderLayout.EAST);
        selectorButton.addActionListener(e -> {
            JInternalFrame selectionDialog = new JInternalFrame("Select Options", true);
            selectionDialog.setLayout(new BorderLayout());
            selectionDialog.setSize(400, 300);
            ArrayList<Object[]> data = new ArrayList<>();
            for (Map.Entry<String, String> entry : options.entrySet()) {
                data.add(new Object[]{false, entry.getKey(), entry.getValue()});
            }
            CustomTable table = new CustomTable(new String[]{"Select", "Key", "Value"}, data);
            JScrollPane scrollPane = new JScrollPane(table);
            selectionDialog.add(scrollPane, BorderLayout.CENTER);
            JButton okButton = new JButton("OK");
            okButton.addActionListener(okEvent -> {
                StringBuilder selectedKeys = new StringBuilder();
                for (int i = 0; i < table.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) table.getValueAt(i, 0);
                    if (isSelected) {
                        String key = table.getValueAt(i, 1).toString();
                        if (!selectedKeys.isEmpty()) {
                            selectedKeys.append(";");
                        }
                        selectedKeys.append(key);
                    }
                }
                textField.setText(selectedKeys.toString());
                selectionDialog.dispose();
            });
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(okButton);
            selectionDialog.add(buttonPanel, BorderLayout.SOUTH);
            selectionDialog.setVisible(true);
        });
        parentFrame.add(container);
        return textField;
    }

    public static JScrollPane scrollPane(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        return scrollPane;
    }
}