package org.Canal.UI.Views.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.BusinessUnits.Transaction;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;

public class ViewTransaction extends LockeState {

    private Ledger ledger;
    private Transaction transaction;
    private RefreshListener refreshListener;

    public ViewTransaction(Ledger ledger, Transaction transaction, RefreshListener refreshListener) {

        super("View Transaction", "/LGS/TRS/" + transaction.getId(), false, true, false, false);
        this.ledger = ledger;
        this.transaction = transaction;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Activity", activity());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel toolbar() {


        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        if (!transaction.getStatus().equals(LockeStatus.SETTLED)) {

            IconButton settle = new IconButton("Settle", "complete", "Settle Transaction");
            settle.addActionListener(_ -> {

                transaction.setStatus(LockeStatus.SETTLED);
                ledger.updateTransaction(transaction);
                if (refreshListener != null) refreshListener.refresh();
                dispose();
            });
            toolbar.add(settle);
            toolbar.add(Box.createHorizontalStrut(5));
        }

        IconButton changeValue = new IconButton("Change", "autoprice", "Set Transaction Value", "/LGS/TRS/CHG");
        changeValue.addActionListener(_ -> {

            String v = JOptionPane.showInputDialog("New Transaction Value");
            if (v != null) {

                transaction.setAmount(Double.parseDouble(v));
                ledger.updateTransaction(transaction);
                if (refreshListener != null) refreshListener.refresh();
                dispose();
            }

        });
        toolbar.add(changeValue);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton zero = new IconButton("Zero", "delinquent", "Zero Transaction", "/LGS/TRS/ZR");
        zero.addActionListener(_ -> {

            int confirm = JOptionPane.showConfirmDialog(this, "Confirm Zeroing Transaction in Ledger");
            if (confirm == JOptionPane.YES_OPTION) {

                transaction.setAmount(0);
                ledger.updateTransaction(transaction);
                if (refreshListener != null) refreshListener.refresh();
                dispose();
            }
        });
        toolbar.add(zero);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find", "/LGS/TRS/F");
        find.addActionListener(_ -> {

        });
        toolbar.add(find);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Transaction", "/LGS/TRS/DEL");
        delete.addActionListener(_ -> {

            int confirm = JOptionPane.showConfirmDialog(this, "Confirm Transaction Deletion in Ledger");
            if (confirm == JOptionPane.YES_OPTION) {

                ledger.removeTransaction(transaction);
                if (refreshListener != null) refreshListener.refresh();
                dispose();
            }
        });
        toolbar.add(delete);
        toolbar.add(Box.createHorizontalStrut(5));

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return general;
    }

    private JPanel activity() {

        JPanel activity = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return activity;
    }
}
