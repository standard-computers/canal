package org.Canal.UI.Elements;

import org.Canal.Models.SupplyChainUnits.Transaction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Frame extends JFrame {

    private JPanel content;

    public Frame(Transaction transaction){
        super(transaction.getName());
        setLayout(new BorderLayout());
        JPanel statusBar = new JPanel(new BorderLayout());
        JLabel trLabel = new JLabel("OK [200]");
        JLabel status = new JLabel(transaction.getCode());
        status.setForeground(new Color(40, 149, 4));
        trLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        status.setBorder(new EmptyBorder(5, 5, 5, 5));
        content = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(content, BorderLayout.CENTER);
        statusBar.add(trLabel, BorderLayout.WEST);
        statusBar.add(status, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public Component add(Component c){
        content.add(c);
        repaint();
        return c;
    }
}