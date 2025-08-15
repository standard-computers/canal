package org.Canal.UI.Views.Distribution;

import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.LockeStatus;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TRANS/$[DLV_TYPE]/$[DELIVERY_ID]
 */
public class ViewDelivery extends LockeState {

    private Delivery delivery;

    public ViewDelivery(Delivery delivery) {

        super("View Delivery", "/TRANS/" + delivery.getType(), false, true, false, true);
        this.delivery = delivery;

        setLayout(new BorderLayout());

        JPanel optionsInfo = new JPanel(new BorderLayout());
        optionsInfo.add(Elements.header("View Delivery", SwingConstants.LEFT), BorderLayout.NORTH);

        add(info(), BorderLayout.CENTER);
        add(optionsInfo, BorderLayout.NORTH);
    }

    private JPanel info(){
        JPanel p = new JPanel();
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("Receipt", receipt());
        tabs.add("Pallets", pallets());
        tabs.add("Items", items());
        p.setLayout(new BorderLayout());
        p.add(toolbar(), BorderLayout.NORTH);
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    private JPanel toolbar(){
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton markDelivered = new IconButton("Mark Delivered", "trucks", "Email Employee");
        IconButton receive = new IconButton("Receive", "invoices", "Receive Delivery (GR)");
        IconButton depart = new IconButton("Depart", "invoices", "Depart Delivery (GI)");
        IconButton writeup = new IconButton("Writeup", "delinquent", "Writeup Employee");
        IconButton suspend = new IconButton("Suspend", "blocked", "Suspend Employee");
        IconButton label = new IconButton("Labels", "label", "Print labels for properties (like for badges)");
        tb.add(markDelivered);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(receive);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(depart);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(writeup);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspend);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        markDelivered.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                delivery.setStatus(LockeStatus.DELIVERED);
                delivery.save();
                dispose();
            }
        });
        receive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        writeup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        suspend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        return tb;
    }

    private JPanel pallets(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel items(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel receipt(){
        JPanel p = new JPanel();
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.append("ID : " + delivery.getId() + "\n");
        textArea.append("Destination : " + delivery.getDestination() + "\n");
        textArea.append("Status : " + delivery.getStatus() + "\n");
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setEditable(false);
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        p.add(scrollPane);
        return p;
    }
}