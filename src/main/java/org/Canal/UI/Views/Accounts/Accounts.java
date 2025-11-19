package org.Canal.UI.Views.Accounts;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finder;
import org.Canal.UI.Views.Invoices.CreateInvoice;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ACCS
 * View list of accounts that are not DELETED, ARCHIVED, or CLOSED.
 */
public class Accounts extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable accountsTable;

    public Accounts(DesktopState desktop) {

        super("Accounts", "/ACCS");
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        accountsTable = table();
        JScrollPane tableScrollPane = new JScrollPane(accountsTable);
        tableScrollPane.setPreferredSize(new Dimension(850, 600));
        holder.add(Elements.header("Accounts", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("ACCS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton importAccounts = new IconButton("Import", "import", "Import from CSV", "/ACCS/EXP");
        importAccounts.addActionListener(_ -> accountsTable.exportToCSV());
        tb.add(importAccounts);
        tb.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV", "/ACCS/EXP");
        export.addActionListener(_ -> accountsTable.exportToCSV());
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("New", "create", "Create Account", "/ACCS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateAccount(desktop, this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton invoice = new IconButton("Invoice", "invoice", "Create Invoice", "/INVS/NEW");
        invoice.addActionListener(_ -> desktop.put(new CreateInvoice(desktop)));
        tb.add(invoice);
        tb.add(Box.createHorizontalStrut(5));

        IconButton open = new IconButton("Open", "open", "Open Account", "/ACCS/O");
        open.addActionListener(_ -> desktop.put(Engine.router("/ACCS/O", desktop)));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Account", "/ACCS/F");
        find.addActionListener(_ -> desktop.put(new Finder("/ACCS", Account.class, desktop)));
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Name",
                "Location",
                "Customer",
                "Opened",
                "Closed",
                "Protected",
                "Agreement",
                "Status",
                "Terms",
                "Invoices",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Account account : Engine.getAccounts()) {

            if (!account.getStatus().equals(LockeStatus.DELETED)
                    && !account.getStatus().equals(LockeStatus.ARCHIVED)
                    && !account.getStatus().equals(LockeStatus.CLOSED)) {

                data.add(new Object[]{
                        account.getId(),
                        account.getName(),
                        account.getLocation(),
                        account.getCustomer(),
                        account.getOpened(),
                        account.getClosed(),
                        account.isPasswordProtected(),
                        account.getAgreement(),
                        account.getStatus(),
                        (account.getTerms() + " DAYS"),
                        Engine.getInvoicesForAccount(account.getId()),
                        account.getCreated(),
                });
            }
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String accountId = String.valueOf(t.getValueAt(r, 1));
                        Account account = Engine.getAccount(accountId);
                        desktop.put(new ViewAccount(account, desktop));
                    }
                }
            }
        });
        return ct;
    }

    @Override
    public void refresh() {

        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) accountsTable.getParent().getParent();
        scrollPane.setViewportView(newTable);
        accountsTable = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}