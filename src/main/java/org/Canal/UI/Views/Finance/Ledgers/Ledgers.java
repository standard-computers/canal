package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
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
 * /GR
 */
public class Ledgers extends LockeState implements RefreshListener {

    private CustomTable table;
    private DesktopState desktop;

    public Ledgers(DesktopState desktop) {

        super("Ledgers", "/LGS", true, true, true, true);
        setFrameIcon(new ImageIcon(Ledgers.class.getResource("/icons/ledgers.png")));
        if(Engine.getLedgers().isEmpty()){
            dispose();
            JOptionPane.showMessageDialog(this, "No Ledgers");
        }
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 600));
        holder.add(Elements.header("Ledgers", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        add(holder);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("LGS", "export_enabled")){
            IconButton export = new IconButton("Import", "export", "Import from CSV");
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("LGS", "export_enabled")){
            IconButton export = new IconButton("Export", "export", "Export as CSV");
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton createLedger = new IconButton("Create", "create", "Resume/Activate PO");
        createLedger.addActionListener(_ -> desktop.put(new CreateLedger(desktop)));
        tb.add(createLedger);
        tb.add(Box.createHorizontalStrut(5));

        IconButton closeLedger = new IconButton("Close", "block", "Close a ledger. Audits and adjustments are complete. No more transactions can be comitted.");
        tb.add(closeLedger);
        tb.add(Box.createHorizontalStrut(5));

        IconButton blockLedger = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        tb.add(blockLedger);
        tb.add(Box.createHorizontalStrut(5));

        IconButton suspendLedger = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        tb.add(suspendLedger);
        tb.add(Box.createHorizontalStrut(5));

        IconButton archivePo = new IconButton("Archive", "archive", "Archive PO, removes");
        tb.add(archivePo);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Ledgers");
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Name",
                "Organization",
                "Period",
                "Starts",
                "Ends",
                "Created",
                "Trans. Count",
                "Status",
        };
        ArrayList<Object[]> ledgers = new ArrayList<>();
        for (Ledger gr : Engine.getLedgers()) {
            ledgers.add(new Object[]{
                    gr.getId(),
                    gr.getName(),
                    gr.getOrganization(),
                    gr.getPeriod(),
                    gr.getStarts(),
                    gr.getEnds(),
                    gr.getCreated(),
                    String.valueOf(gr.getTransactions().size()),
                    String.valueOf(gr.getStatus())
            });
        }
        CustomTable ct = new CustomTable(columns, ledgers);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        Ledger l = Engine.getLedger(value);
                        desktop.put(new ViewLedger(l, desktop));
                    }
                }
            }
        });
        return ct;
    }

    @Override
    public void refresh() {

    }
}