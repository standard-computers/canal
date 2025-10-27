package org.Canal.UI.Views.Accounts;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class ViewAccount extends LockeState {

    private Account account;
    private DesktopState desktop;

    public ViewAccount(Account account, DesktopState desktop) {

        super(account.getName(), "/ACCS/" + account.getId());
        setFrameIcon(new ImageIcon(ViewAccount.class.getResource("/icons/windows/locke.png")));
        this.account = account;
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Activity", activity());
        tabs.addTab("Invoices", invoices());
        tabs.addTab("Payments", payments());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel activity() {

        JPanel activity = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return activity;
    }

    private JPanel invoices() {

        JPanel invoices = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return invoices;
    }


    private JPanel payments() {

        JPanel payments = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return payments;
    }

    private RTextScrollPane notes() {

        RTextScrollPane notes = Elements.simpleEditor();
        notes.getTextArea().setText(account.getNotes());
        return notes;
    }
}