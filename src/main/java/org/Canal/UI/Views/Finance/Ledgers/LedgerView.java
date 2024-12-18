package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.BusinessUnits.Transaction;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /LGS/$[LEDGER_ID]
 */
public class LedgerView extends LockeState implements RefreshListener {

    private Ledger ledger;
    private DesktopState desktop;
    private CustomTable table;

    public LedgerView(Ledger ledger, DesktopState desktop) {
        super("Ledger", "/LGS/$", true, true, true, true);
        this.ledger = ledger;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(LedgerView.class.getResource("/icons/distribution_centers.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header(ledger.getName() + " / " + ledger.getId(), SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                    }
                }
            }
        });
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton createDc = new IconButton("New", "order", "Create a Location", "/G/NEW");
        IconButton modifyDc = new IconButton("Make Adjustment", "modify", "Modify a Location", "/G/MOD");
        IconButton archiveDc = new IconButton("Archive", "archive", "Archive a Location", "/G/ARCHV");
        IconButton removeDc = new IconButton("Remove", "delete", "Delete a Location", "/G/DEL");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRefresh();
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{"ID", "Description", "User", "Locke", "Type", "Location", "Reference", "Amount", "Committed", "Settled", "Status"};
        ArrayList<Object[]> data = new ArrayList<>();
        for (Transaction t : Engine.getLedger(ledger.getId()).getTransactions()) {
            data.add(new Object[]{
                    t.getId(),
                    t.getName(),
                    t.getUser(),
                    t.getLocke(),
                    t.getObjex(),
                    t.getLocation(),
                    t.getReference(),
                    t.getAmount(),
                    t.getCommitted(),
                    t.getSettled(),
                    t.getStatus(),
            });
        }
        return new CustomTable(columns, data);
    }

    @Override
    public void onRefresh() {
        CustomTable newTable = createTable();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router("/LGS/TRANS/" + value, desktop));
                    }
                }
            }
        });
    }
}