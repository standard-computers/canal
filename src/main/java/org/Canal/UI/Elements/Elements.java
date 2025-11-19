package org.Canal.UI.Elements;

import com.formdev.flatlaf.ui.FlatBorder;
import org.Canal.UI.ColorUtil;
import org.Canal.Utils.Engine;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
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

    public static JLabel label(String text) {

        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(0, 10, 10, 0));
        return label;
    }

    public static JLabel inputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(
                UIManager.getFont("Label.font").getName(),
                Font.PLAIN,
                Engine.getConfiguration().getFontSize()
        ));
        label.setMinimumSize(new Dimension(120, 25));
        label.setMaximumSize(new Dimension(200, 25));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Label.foreground").brighter()));

        return label;
    }

    public static JLabel inputLabel(String text, String help) {

        JLabel label = inputLabel(text);

        // Allow focus
        label.setFocusable(true);

        // Optional: add a subtle focus indicator (bottom border highlight)
        Color normalBorder = UIManager.getColor("Label.foreground");
        Color focusBorder = UIManager.getColor("Label.focus") != null
                ? UIManager.getColor("Label.focus")
                : UIManager.getColor("TextField.selectionBackground");

        label.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, focusBorder));
            }

            @Override
            public void focusLost(FocusEvent e) {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, normalBorder));
            }
        });

        // Key listener for F1 help
        label.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    // You can swap JOptionPane for your custom message system
                    JOptionPane.showMessageDialog(label, help, label.getText(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JOptionPane.showMessageDialog(label, help, label.getText(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Optional: make focus visible via keyboard navigation
        label.setFocusTraversalKeysEnabled(true);

        return label;
    }

    public static JLabel h2(String text) {

        JLabel label = new JLabel(text);
        label.setFont(UIManager.getFont("h2.font"));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
    }

    public static JLabel h3(String text) {

        JLabel label = new JLabel(text);
        label.setFont(UIManager.getFont("h3.font"));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
    }

    public static JLabel h3(String text, Color color) {

        JLabel label = new JLabel(text);
        label.setFont(UIManager.getFont("h3.font"));
        label.setForeground(color);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
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
     *
     * @param text Header Text
     * @return JPanel
     */
    public static JPanel header(String text) {

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

    public static RTextScrollPane simpleEditor() {

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        textArea.setCodeFoldingEnabled(true);
        textArea.setLineWrap(false);
        return new RTextScrollPane(textArea);
    }

    public static JScrollPane scrollPane(JComponent content) {

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        return scrollPane;
    }
}