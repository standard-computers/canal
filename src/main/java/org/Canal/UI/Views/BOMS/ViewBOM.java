package org.Canal.UI.Views.BOMS;

import org.Canal.Models.SupplyChainUnits.BillOfMaterials;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel header() {

        JPanel header = new JPanel(new BorderLayout());
        JPanel itemInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.inputLabel("ID"), new Copiable(billOfMaterials.getId()));
        form.addInput(Elements.inputLabel("BOM Name"), new Copiable(billOfMaterials.getName()));
        form.addInput(Elements.inputLabel("Production Location"), new Copiable(billOfMaterials.getLocation()));
        form.addInput(Elements.inputLabel("Finished Item ID"), new Copiable(billOfMaterials.getItem()));
        form.addInput(Elements.inputLabel("Customer"), new Copiable(billOfMaterials.getCustomer()));
        itemInfo.add(form);

        header.add(itemInfo, BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.NORTH);

        return header;
    }


    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        if (billOfMaterials.getStatus().equals(LockeStatus.NEW)) {

            IconButton activate = new IconButton("Activate", "start", "Activate BOM for Production");
            activate.addActionListener(_ -> {

                billOfMaterials.setStatus(LockeStatus.ACTIVE);
                billOfMaterials.save();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
                dispose();
            });
            tb.add(activate);
            tb.add(Box.createHorizontalStrut(5));
        }

        if (billOfMaterials.getStatus().equals(LockeStatus.ACTIVE)) {

            IconButton deactivate = new IconButton("Deactivate", "suspend", "Deactivate BOM for Production");
            deactivate.addActionListener(_ -> {

                billOfMaterials.setStatus(LockeStatus.SUSPENDED);
                billOfMaterials.save();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
                dispose();
            });
            tb.add(deactivate);
            tb.add(Box.createHorizontalStrut(5));
        }

        if (!billOfMaterials.getStatus().equals(LockeStatus.ACTIVE)) {

            IconButton archive = new IconButton("Archive", "archive", "Archive Bill of Materials", "/BOMS/ARCHV");
            archive.addActionListener(_ -> {

                billOfMaterials.setStatus(LockeStatus.ARCHIVED);
                billOfMaterials.save();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
                dispose();
            });
            tb.add(archive);
            tb.add(Box.createHorizontalStrut(5));

            IconButton delete = new IconButton("Delete", "delete", "Delete Bill of Materials", "/BOMS/DEL");
            delete.addActionListener(_ -> {

                billOfMaterials.setStatus(LockeStatus.DELETED);
                billOfMaterials.save();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
                dispose();
            });
            tb.add(delete);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton modify = new IconButton("Modify", "modify", "/BOMS/MOD");
        modify.addActionListener(_ -> desktop.put(new ModifyBOM(billOfMaterials, desktop, refreshListener)));
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_E, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                modify.doClick();
            }
        });

        toolbar.add(Elements.header(billOfMaterials.getId() + " " + billOfMaterials.getName(), SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private JScrollPane components() {

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
        for (int s = 0; s < billOfMaterials.getComponents().size(); s++) {
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
        return new JScrollPane(ct);
    }

    private JScrollPane steps() {

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
        for (int s = 0; s < billOfMaterials.getSteps().size(); s++) {
            Task ol = billOfMaterials.getSteps().get(s);
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    ol.getName(),
                    ol.getDescription(),
                    ol.getLocation(),
                    ol.getSourceArea(),
                    ol.getSourceBin(),
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

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.inputLabel("Status"), new Copiable(String.valueOf(billOfMaterials.getStatus())));
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        RTextScrollPane notes = Elements.simpleEditor();
        notes.getTextArea().setText(billOfMaterials.getNotes());
        notes.getTextArea().setEditable(false);
        return notes;
    }
}