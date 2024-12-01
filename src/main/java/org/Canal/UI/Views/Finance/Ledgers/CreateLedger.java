package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.DatePicker;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

/**
 * /LGS/NEW
 */
public class CreateLedger extends JInternalFrame {

    public CreateLedger(DesktopState desktop) {

        super("Create Ledger", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLedger.class.getResource("/icons/create.png")));

        Form f = new Form();
        JTextField ledgerIdField = Elements.input(String.valueOf(LocalDate.now().getYear()), 10);
        Selectable organizations = Selectables.organizations();
        JTextField ledgerNameField = Elements.input("FY" + LocalDate.now().getYear(), 10);
        Selectable periods = Selectables.periods();
        periods.editable();
        DatePicker ledgerStartPicker = new DatePicker();
        DatePicker ledgerEndPicker = new DatePicker();

        f.addInput(new Label("*New Ledger ID", UIManager.getColor("Label.foreground")), ledgerIdField);
        f.addInput(new Label("Organization", Constants.colors[0]), organizations);
        f.addInput(new Label("Ledger Name", Constants.colors[1]), ledgerNameField);
        f.addInput(new Label("Period", Constants.colors[2]), periods);
        f.addInput(new Label("Start Date", Constants.colors[3]), ledgerStartPicker);
        f.addInput(new Label("Close Date", Constants.colors[4]), ledgerEndPicker);

        JButton create = Elements.button("Create");
        setLayout(new BorderLayout());
        add(Elements.header("Create Ledger"), BorderLayout.NORTH);
        add(create, BorderLayout.SOUTH);
        add(f, BorderLayout.CENTER);

        create.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                String ledgerId = ledgerIdField.getText().trim();
                String ledgerName = ledgerNameField.getText().trim();
                String org = organizations.getSelectedValue();
                String period = periods.getSelectedValue();

                Ledger l = new Ledger();
                l.setId(ledgerId);
                l.setName(ledgerName);
                l.setOrg(org);
                l.setPeriod(period);
                l.setStarts(ledgerStartPicker.getSelectedDateString());
                l.setEnds(ledgerEndPicker.getSelectedDateString());
                l.setCreated(Constants.now());
                l.setStatus(LockeStatus.NEW);

                Pipe.save("/LGS", l);
                dispose();
                desktop.put(new LedgerView(l, desktop));
            }
        });
    }
}