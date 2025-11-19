package org.Canal.UI.Views.Rates;

import org.Canal.Models.BusinessUnits.Rate;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * /RTS/NEW
 */
public class CreateRate extends LockeState {

    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField rateId;
    private JTextField rateName;
    private JTextField rateDescription;
    private JCheckBox rateIsPercent;
    private JTextField rateValue;
    private Selectable objexes;
    private JTextField rateReference;
    private JCheckBox rateIsTax;

    public CreateRate(DesktopState desktop, RefreshListener refreshListener) {

        super("Create Rate", "/RTS/NEW", false, true, false, true);
        setLayout(new GridBagLayout());
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        rateId = Elements.input(15);
        rateName = Elements.input();
        rateDescription = Elements.input();
        rateIsPercent = new JCheckBox();
        rateValue = Elements.input();
        objexes = Selectables.objexForRates();
        rateReference = Elements.input();
        rateIsTax = new JCheckBox();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Rate ID"), rateId);
        form.addInput(Elements.inputLabel("Name"), rateName);
        form.addInput(Elements.inputLabel("Description"), rateDescription);
        form.addInput(Elements.inputLabel("Percent"), rateIsPercent);
        form.addInput(Elements.inputLabel("Value"), rateValue);
        form.addInput(Elements.inputLabel("Objex"), objexes);
        form.addInput(Elements.inputLabel("Reference"), rateReference);
        form.addInput(Elements.inputLabel("Tax"), rateIsTax);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Rate");
        copyFrom.addActionListener(_ -> {

            String rateId = JOptionPane.showInputDialog(CreateRate.this, "Enter Rate ID");
            Rate rate = Engine.getRate(rateId);
            rateName.setText(rate.getName());
            rateDescription.setText(rate.getDescription());
            rateIsPercent.setSelected(rate.isPercent());
            rateValue.setText(String.valueOf(rate.getValue()));
            objexes.setSelectedValue(rate.getObjex());
            rateReference.setText(rate.getReference());
            rateIsTax.setSelected(rate.isTax());
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(copyFrom);

        IconButton review = new IconButton("Review", "review", "Review Rate");
        review.addActionListener(_ -> performReview());
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(review);

        IconButton execute = new IconButton("Execute", "execute", "Create Rate");
        execute.addActionListener(_ -> {

            Rate newRate = new Rate();
            newRate.setId(rateId.getText());
            newRate.setName(rateName.getText());
            newRate.setDescription(rateDescription.getText());
            newRate.setPercent(rateIsPercent.isSelected());
            newRate.setValue(Double.parseDouble(rateValue.getText()));
            newRate.setObjex(objexes.getSelectedValue());
            newRate.setReference(rateReference.getText());
            newRate.setTax(rateIsTax.isSelected());
            Pipe.save("/RTS", newRate);

            dispose();
            if (refreshListener != null) {
                refreshListener.refresh();
            }

            if ((boolean) Engine.codex.getValue("RTS", "auto_open_new")) {

                desktop.put(new ViewRate(newRate, desktop, refreshListener));
            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(execute);

        toolbar.add(buttons, BorderLayout.SOUTH);
        toolbar.add(Elements.header("Create a Rate", SwingConstants.LEFT), BorderLayout.NORTH);

        return toolbar;
    }

    private void performReview() {

        if (rateId.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Rate/Tax MUST have an ID"});
        }

        if (rateName.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Rate or tax name not set. Are you sure?"});
        }

        if (rateValue.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Rate/Tax must have value"});
        }

        if (rateIsPercent.isSelected()) {
            if (Double.parseDouble(rateValue.getText()) >= 1) {
                addToQueue(new String[]{"CRITICAL", "Rate/Tax value as percent >= 1 will be >= 100%. Are you sure?"});
            }
        }

        if (rateIsPercent.isSelected()) {
            if (rateIsTax.isSelected()) {
                if (Double.parseDouble(rateValue.getText()) >= 1) {
                    addToQueue(new String[]{"CRITICAL", "Tax is >= 1 will be >= 100%. Are you sure?"});
                }
            }
        }

        if (objexes.getSelectedValue().isEmpty()) {
            addToQueue(new String[]{"WARNING", "An Objex is not referenced. Are you sure?"});
        } else {
            if (rateReference.getText().isEmpty()) {
                addToQueue(new String[]{"WARNING", "An Objex is referenced without value in reference. Are you sure?"});
            }
        }

        if (rateReference.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Rate/Tax reference not set, are you sure?"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}