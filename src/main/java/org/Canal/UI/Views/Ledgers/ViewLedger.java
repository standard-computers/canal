package org.Canal.UI.Views.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.BusinessUnits.Transaction;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /LGS/$[LEDGER_ID]
 */
public class ViewLedger extends LockeState implements RefreshListener {

    private Ledger ledger;
    private DesktopState desktop;
    private CustomTable table;

    public ViewLedger(Ledger ledger, DesktopState desktop) {

        super("Ledger", "/LGS/" + ledger.getId());
        setFrameIcon(new ImageIcon(ViewLedger.class.getResource("/icons/windows/locke.png")));
        this.ledger = ledger;
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header(ledger.getName() + " / " + ledger.getId(), SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("LGS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV");
        export.addActionListener(_ -> table.exportToCSV());
        toolbar.add(export);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "Modify Ledger", "/LGS/MOD");
        modify.addActionListener(_ -> {});
        toolbar.add(modify);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton makeAdjustment = new IconButton("Make Adjustment", "autoprice", "Make Adjustment", "/LGS/ADJ");
        makeAdjustment.addActionListener(_ -> {});
        toolbar.add(makeAdjustment);
        toolbar.add(Box.createHorizontalStrut(5));

        if (!ledger.getStatus().equals(LockeStatus.SETTLED)) {

            IconButton settle = new IconButton("Settle", "complete", "Settle Transactions", "/LGS/TRS/STL");
            settle.addActionListener(_ -> {

                for (int i = 0; i < ledger.getTransactions().size(); i++) {
                    if (!ledger.getTransactions().get(i).getStatus().equals(LockeStatus.SETTLED)) {
                        ledger.getTransactions().get(i).setStatus(LockeStatus.SETTLED);
                        ledger.getTransactions().get(i).setSettled(Constants.now());
                    }
                }
                ledger.setStatus(LockeStatus.SETTLED);
                ledger.save();
                refresh();
                settle.setVisible(false);
            });
            toolbar.add(settle);
            toolbar.add(Box.createHorizontalStrut(5));
        }

        IconButton addTransaction = new IconButton("Add Transaction", "create", "Add a Transaction", "/LGS/TRS/ADD");
        addTransaction.addActionListener(_ -> {});
        toolbar.add(addTransaction);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton valueate = new IconButton("Balance", "autoprice", "Evaluate Balance");
        valueate.addActionListener(_ -> {
            double value = 0.0;
            for (Transaction t : ledger.getTransactions()) {
                value += t.getAmount();
            }
            JOptionPane.showMessageDialog(null, "$" + value + "\n" + ledger.getName() + " / " + ledger.getId() + " / " + Constants.now());
        });
        toolbar.add(valueate);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        toolbar.add(refresh);
        toolbar.add(Box.createHorizontalStrut(5));

        return toolbar;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Description",
                "User",
                "Locke",
                "Type",
                "Location",
                "Reference",
                "Amount",
                "Committed",
                "Settled",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Transaction t : Engine.getLedger(ledger.getId()).getTransactions()) {
            data.add(new Object[]{
                    t.getId(),
                    t.getName(),
                    t.getOwner(),
                    t.getLocke(),
                    t.getObjex(),
                    t.getLocation(),
                    t.getReference(),
                    t.getAmount(),
                    t.getCommitted(),
                    t.getSettled(),
                    t.getStatus(),
                    t.getCreated(),
            });
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        for (Transaction t : ledger.getTransactions()) {
                            if (t.getId().equals(value)) {
                                desktop.put(new ViewTransaction(ledger, t, ViewLedger.this));
                            }
                        }
                    }
                }
            }
        });
        return ct;
    }

    @Override
    public void refresh() {

        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}