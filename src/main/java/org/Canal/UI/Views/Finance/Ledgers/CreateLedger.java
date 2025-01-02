package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
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
 * Make a new Ledger for an Organization
 */
public class CreateLedger extends LockeState {

    public CreateLedger(DesktopState desktop) {

        super("Create Ledger", "/LGS/NEW", false, true, false, true);
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
                String startDate = ledgerStartPicker.getSelectedDateString();
                String endDate = ledgerEndPicker.getSelectedDateString();

                Ledger newLedger = new Ledger();
                newLedger.setId(ledgerId);
                newLedger.setName(ledgerName);
                newLedger.setOrganization(org);
                newLedger.setPeriod(period);
                newLedger.setStarts(startDate);
                newLedger.setEnds(endDate);
                newLedger.setCreated(Constants.now());
                newLedger.setStatus(LockeStatus.ACTIVE);

                Pipe.save("/LGS", newLedger);
                dispose();
                desktop.put(new ViewLedger(newLedger, desktop));
            }
        });
    }
}