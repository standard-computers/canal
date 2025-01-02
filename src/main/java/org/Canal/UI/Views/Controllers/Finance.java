package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finance.GoodsIssues.GoodsIssues;
import org.Canal.UI.Views.Finance.GoodsReceipts.GoodsReceipts;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.PurchaseRequisitions;
import org.Canal.UI.Views.Finance.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.CreatePurchaseRequisition;
import org.Canal.UI.Views.Finance.SalesOrders.CreateSalesOrder;
import org.Canal.UI.Views.Finance.SalesOrders.SalesOrders;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CNL/FI
 */
public class Finance extends LockeState {

    public Finance(DesktopState desktop) {

        super("Finance", "/CNL/FI", true, true, true, true);
        setFrameIcon(new ImageIcon(TimeClock.class.getResource("/icons/finance.png")));

        JPanel main = new JPanel(new GridLayout(1, 4));

        JPanel demand = new JPanel(new GridLayout(2, 1));
        demand.setBorder(new TitledBorder("Demand"));
        JButton newPurReg = Elements.button("New Purchase Req");
        JButton purReg = Elements.button("Purchase Reqs");
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

        JPanel sales = new JPanel(new GridLayout(2, 1));
        sales.setBorder(new TitledBorder("Sales"));
        JButton createSalesOrder = Elements.button("New Sales Orders");
        JButton salesOrders = Elements.button("Sales Orders");
        sales.add(createSalesOrder);
        sales.add(salesOrders);
        createSalesOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateSalesOrder());
            }
        });
        salesOrders.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new SalesOrders(desktop));
            }
        });

        JPanel planning = new JPanel(new GridLayout(4, 1));
        planning.setBorder(new TitledBorder("Planning"));
        JButton newPurOrd = Elements.button("New Purchase Order");
        JButton purOrd = Elements.button("Purchase Orders");
        JButton goodsReceipts = Elements.button("Goods Receipts");
        JButton goodsIssues = Elements.button("Goods Issues");
        planning.add(newPurOrd);
        planning.add(purOrd);
        planning.add(goodsReceipts);
        planning.add(goodsIssues);
        newPurOrd.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder(desktop));
            }
        });
        purOrd.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        goodsReceipts.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new GoodsReceipts(desktop));
            }
        });
        goodsIssues.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new GoodsIssues(desktop));
            }
        });

        JPanel reporting = new JPanel(new GridLayout(8, 1));
        reporting.setBorder(new TitledBorder("Reporting"));
        JButton assetsReporting = Elements.button("Assets");
        JButton liabilitiesReporting = Elements.button("Liabilities");
        JButton equityReporting = Elements.button("Equity");
        JButton revenueReporting = Elements.button("Revenue");
        JButton cogsReporting = Elements.button("COGS");
        JButton operatingExpensesReporting =  Elements.button("Operating Expenses");
        JButton balanceReporting = Elements.button(" PnL/Balance");
        reporting.add(assetsReporting);
        reporting.add(liabilitiesReporting);
        reporting.add(equityReporting);
        reporting.add(revenueReporting);
        reporting.add(cogsReporting);
        reporting.add(operatingExpensesReporting);
        reporting.add(balanceReporting);

        main.add(demand);
        main.add(sales);
        main.add(planning);
        main.add(reporting);
        add(main);
    }
}