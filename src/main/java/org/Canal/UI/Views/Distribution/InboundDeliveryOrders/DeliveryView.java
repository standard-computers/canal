package org.Canal.UI.Views.Distribution.InboundDeliveryOrders;

import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/$[DLV_TYPE]/$[DELIVERY_ID]
 */
public class DeliveryView extends LockeState {

    private Delivery delivery;

    public DeliveryView(Delivery delivery) {

        super("View Delivery", "/TRANS/" + delivery.getType(), false, true, false, true);
        this.delivery = delivery;

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Receipt", receipt());
        tabs.add("Pallets", pallets());
        tabs.add("Items", items());
        setLayout(new BorderLayout());

        JPanel optionsInfo = new JPanel(new BorderLayout());
        optionsInfo.add(Elements.header("View Delivery", SwingConstants.LEFT), BorderLayout.NORTH);

        add(tabs, BorderLayout.CENTER);
        add(optionsInfo, BorderLayout.NORTH);
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