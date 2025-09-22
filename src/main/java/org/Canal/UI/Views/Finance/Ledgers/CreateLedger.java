package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Date;

/**
 * /LGS/NEW
 * Make a new Ledger for an Organization
 */
public class CreateLedger extends LockeState {

    private DesktopState desktop;
    private JTextField ledgerIdField;
    private Selectable organizations;
    private JTextField ledgerNameField;
    private JTextField locationField;
    private Selectable periods;
    private DatePicker ledgerStartPicker;
    private DatePicker ledgerEndPicker;

    public CreateLedger(DesktopState desktop) {

        super("Create Ledger", "/LGS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLedger.class.getResource("/icons/create.png")));
        this.desktop = desktop;

        ledgerIdField = Elements.input(String.valueOf(LocalDate.now().getYear()), 10);
        organizations = Selectables.organizations();
        ledgerNameField = Elements.input("FY" + LocalDate.now().getYear(), 10);
        locationField = Elements.input();
        periods = Selectables.periods();
        periods.editable();
        ledgerStartPicker = new DatePicker();
        ledgerEndPicker = new DatePicker();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("*New Ledger ID", UIManager.getColor("Label.foreground")), ledgerIdField);
        form.addInput(Elements.coloredLabel("Organization", Constants.colors[0]), organizations);
        form.addInput(Elements.coloredLabel("Ledger Name", Constants.colors[1]), ledgerNameField);
        form.addInput(Elements.coloredLabel("Location", Constants.colors[2]), locationField);
        form.addInput(Elements.coloredLabel("Period", Constants.colors[3]), periods);
        form.addInput(Elements.coloredLabel("Start Date", Constants.colors[4]), ledgerStartPicker);
        form.addInput(Elements.coloredLabel("Close Date", Constants.colors[5]), ledgerEndPicker);

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel p = new JPanel(new FlowLayout());
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Review for errors or warnings");
        copyFrom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String ledgerId = JOptionPane.showInputDialog("Enter Ledger ID");
                Ledger cpo = Engine.getLedger(ledgerId);
                if (cpo != null) {

                    ledgerIdField.setText(ledgerId);
                    organizations.setSelectedValue(cpo.getOrganization());
                    ledgerNameField.setText(cpo.getName());
                    locationField.setText(cpo.getLocation());
                    periods.setSelectedValue(cpo.getPeriod());
                    ledgerStartPicker.setSelectedDate(new Date(cpo.starts()));
                    ledgerEndPicker.setSelectedDate(new Date(cpo.ends()));
                } else {
                    JOptionPane.showMessageDialog(CreateLedger.this, "Invalid Ledger ID");
                }
            }
        });
        p.add(copyFrom);
        p.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review for errors or warnings");
        review.addActionListener(_ -> performReview());
        p.add(review);
        p.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Ledger");
        create.addActionListener(_ -> {

            String ledgerId = ledgerIdField.getText().trim();
            String org = organizations.getSelectedValue();
            String ledgerName = ledgerNameField.getText().trim();
            String ledgerLocation = locationField.getText().trim();
            String period = periods.getSelectedValue();
            String startDate = ledgerStartPicker.getSelectedDateString();
            String endDate = ledgerEndPicker.getSelectedDateString();

            Ledger newLedger = new Ledger();
            newLedger.setId(ledgerId);
            newLedger.setName(ledgerName);
            newLedger.setOrganization(org);
            newLedger.setLocation(ledgerLocation);
            newLedger.setPeriod(period);
            newLedger.setStarts(startDate);
            newLedger.setEnds(endDate);
            newLedger.setCreated(Constants.now());
            newLedger.setStatus(LockeStatus.ACTIVE);

            Pipe.save("/LGS", newLedger);
            dispose();
            desktop.put(new ViewLedger(newLedger, desktop));
        });
        p.add(create);
        p.add(Box.createHorizontalStrut(5));

        toolbar.add(Elements.header("Create Ledger", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(p, BorderLayout.SOUTH);

        return toolbar;
    }

    private void performReview() {

    }
}