package org.Canal.UI.Views.BOMS;

import org.Canal.Models.SupplyChainUnits.BillOfMaterials;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /BOMS/$[BOM_ID]
 */
public class ViewBOM extends LockeState {

    //Operating Objects
    private BillOfMaterials billOfMaterials;
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private CustomTable bomsView;
    private CustomTable stepsView;

    public ViewBOM(BillOfMaterials billOfMaterials, DesktopState desktop, RefreshListener refreshListener) {

        super("View Bill of Materials", "/BOMS/" + billOfMaterials.getId());
        setFrameIcon(new ImageIcon(ViewBOM.class.getResource("/icons/windows/boms.png")));
        this.billOfMaterials = billOfMaterials;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Components", components());
        tabs.addTab("Steps", steps());
        tabs.addTab("Controls", controls());

        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel header(){

        JPanel header = new JPanel(new BorderLayout());
        JPanel itemInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("BOM Name", UIManager.getColor("Label.foreground")), new Copiable(billOfMaterials.getName()));
        form.addInput(Elements.coloredLabel("Production Location", UIManager.getColor("Label.foreground")), new Copiable(billOfMaterials.getLocation()));
        form.addInput(Elements.coloredLabel("Finished Item ID", UIManager.getColor("Label.foreground")), new Copiable(billOfMaterials.getItem()));
        form.addInput(Elements.coloredLabel("Customer", UIManager.getColor("Label.foreground")), new Copiable(billOfMaterials.getCustomer()));
        itemInfo.add(form);

        header.add(itemInfo, BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.NORTH);

        return header;
    }


    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Form BOM");
        copyFrom.addActionListener(_ -> {

        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "/BOMS/MOD");
        modify.addActionListener(_ -> desktop.put(new ModifyBOM(billOfMaterials, desktop, refreshListener)));
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));

        toolbar.add(Elements.header(billOfMaterials.getId() + " " + billOfMaterials.getName(), SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private CustomTable componentsTable() {

        String[] columns = new String[]{
                "Component",
                "Item ID",
                "Item Name",
                "Vendor",
                "Req Qty",
                "Qty UOM",
                "Price",
                "Total"
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for(int s = 0; s < billOfMaterials.getComponents().size(); s++){
            StockLine ol = billOfMaterials.getComponents().get(s);
            Item i = Engine.getItem(ol.getItem());
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    ol.getItem(),
                    ol.getName(),
                    i.getVendor(),
                    ol.getQuantity(),
                    ol.getUnitOfMeasure(),
                    ol.getValue(),
                    (ol.getValue() * ol.getQuantity())
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));

                    }
                }
            }
        });
        return ct;
    }

    private JPanel components(){

        JPanel bom = new JPanel(new BorderLayout());
        bomsView = componentsTable();
        bom.add(new JScrollPane(bomsView), BorderLayout.CENTER);

        return bom;
    }

    private JScrollPane steps(){

        String[] columns = new String[]{
                "Step",
                "Name",
                "Description",
                "Location",
                "Area",
                "Bin",
                "Locke",
                "Item",
                "Quantity",
                "Employees",
                "GI",
                "GR"
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for(int s = 0; s < billOfMaterials.getSteps().size(); s++){
            Task ol = billOfMaterials.getSteps().get(s);
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    ol.getName(),
                    ol.getDescription(),
                    ol.getLocation(),
                    ol.getArea(),
                    ol.getBin(),
                    ol.getLocke(),
                    ol.getItem(),
                    ol.getQuantity(),
                    ol.getEmployees(),
                    ol.doesGoodsIssue(),
                    ol.doesGoodsReceipt()
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));

                    }
                }
            }
        });

        return new JScrollPane(ct);
    }


    private JPanel controls(){

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(billOfMaterials.getStatus())));
        controls.add(form);

        return controls;
    }
}