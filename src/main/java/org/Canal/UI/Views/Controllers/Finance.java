package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Views.Orders.PurchaseRequisitions.PurchaseRequisitions;
import org.Canal.UI.Views.Orders.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.UI.Views.Orders.PurchaseRequisitions.CreatePurchaseRequisition;
import org.Canal.Utils.DesktopState;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /FIN
 */
public class Finance extends JInternalFrame {

    public Finance(DesktopState desktop) {
        super("Finance", true, true, true, true);
        setFrameIcon(new ImageIcon(TimeClock.class.getResource("/icons/finance.png")));
        JPanel main = new JPanel(new GridLayout(1, 3));
        JPanel demand = new JPanel(new GridLayout(2, 1));
        demand.setBorder(new TitledBorder("Demand"));
        Button newPurReg = new Button("New Purchase Req");
        Button purReg = new Button("Purchase Reqs");
        demand.add(newPurReg);
        demand.add(purReg);
        newPurReg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseRequisition());
            }
        });
        purReg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new PurchaseRequisitions(desktop));
            }
        });

        JPanel planning = new JPanel(new GridLayout(4, 1));
        planning.setBorder(new TitledBorder("Planning"));
        Button newPurOrd = new Button("New Purchase Order");
        Button purOrd = new Button("Purchase Orders");
        Button goodsReceipts = new Button("Goods Receipts");
        Button goodsIssues = new Button("Goods Issues");
        planning.add(newPurOrd);
        planning.add(purOrd);
        planning.add(goodsReceipts);
        planning.add(goodsIssues);
        newPurOrd.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder());
            }
        });
        purOrd.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });

        JPanel reporting = new JPanel(new GridLayout(8, 1));
        reporting.setBorder(new TitledBorder("Reporting"));
        Button assetsReporting = new Button("Assets");
        Button liabilitiesReporting = new Button("Liabilities");
        Button equityReporting = new Button("Equity");
        Button revenueReporting = new Button("Revenue");
        Button cogsReporting = new Button("COGS");
        Button operatingExpensesReporting =  new Button("Operating Expenses");
        Button balanceReporting = new Button(" PnL/Balance");
        reporting.add(assetsReporting);
        reporting.add(liabilitiesReporting);
        reporting.add(equityReporting);
        reporting.add(revenueReporting);
        reporting.add(cogsReporting);
        reporting.add(operatingExpensesReporting);
        reporting.add(balanceReporting);

        main.add(demand);
        main.add(planning);
        main.add(reporting);
        add(main);
    }
}