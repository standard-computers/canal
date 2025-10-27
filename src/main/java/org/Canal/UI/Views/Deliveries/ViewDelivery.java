package org.Canal.UI.Views.Deliveries;

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

/**
 * /TRANS/$[DLV_TYPE]/$[DELIVERY_ID]
 */
public class ViewDelivery extends LockeState {

    private Delivery delivery;

    public ViewDelivery(Delivery delivery) {

        super("View Delivery", "/" + delivery.getType() + "/" + delivery.getId());
        this.delivery = delivery;

        setLayout(new BorderLayout());

        JPanel optionsInfo = new JPanel(new BorderLayout());
        optionsInfo.add(Elements.header("View Delivery", SwingConstants.LEFT), BorderLayout.NORTH);

        add(general(), BorderLayout.CENTER);
        add(optionsInfo, BorderLayout.NORTH);
    }

    private JPanel general() {

        JPanel general = new JPanel();
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("Receipt", receipt());
        tabs.add("Pallets", pallets());
        tabs.add("Items", items());
        general.setLayout(new BorderLayout());
        general.add(toolbar(), BorderLayout.NORTH);
        general.add(tabs, BorderLayout.CENTER);
        return general;
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton markDelivered = new IconButton("Mark Delivered", "trucks", "Email Employee");
        markDelivered.addActionListener(_ -> {
            delivery.setStatus(LockeStatus.DELIVERED);
            delivery.save();
            dispose();
        });
        tb.add(markDelivered);
        tb.add(Box.createHorizontalStrut(5));

        IconButton receive = new IconButton("Receive", "invoices", "Receive Delivery (GR)");
        tb.add(receive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton depart = new IconButton("Depart", "invoices", "Depart Delivery (GI)");
        tb.add(depart);
        tb.add(Box.createHorizontalStrut(5));

        IconButton writeup = new IconButton("Writeup", "delinquent", "Writeup Employee");
        tb.add(writeup);
        tb.add(Box.createHorizontalStrut(5));

        IconButton suspend = new IconButton("Suspend", "blocked", "Suspend Employee");
        tb.add(suspend);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Labels", "barcodes", "Print labels for properties (like for badges)");
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private JPanel pallets() {
        JPanel p = new JPanel();

        return p;
    }

    private JPanel items() {
        JPanel p = new JPanel();

        return p;
    }

    private JPanel receipt() {
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