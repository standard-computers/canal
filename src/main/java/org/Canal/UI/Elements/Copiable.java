package org.Canal.UI.Elements;

import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Copiable extends JTextField {

    public Copiable(String value) {

        super(value);
        if (value != null && value.equals("false")) {

            setForeground(Color.RED);

        } else if (value != null && value.equals("true")) {

            setForeground(new Color(11, 111, 9));
        }
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        setEditable(false);
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    String text = getText();
//                    copyToClipboard(text);
//                }
//            }
//        });
    }

    public Copiable(String value, MouseAdapter adapter) {
        this(value);
        addMouseListener(adapter);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        System.out.println("Copied to clipboard: " + text);
    }

    public String value() {
        return getText();
    }
}