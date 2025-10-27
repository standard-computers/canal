package org.Canal.UI.Views.Rates;

import org.Canal.Models.BusinessUnits.Rate;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * /RTS/$[RATE_ID]
 */
public class ViewRate extends LockeState {

    private Rate rate;
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JCheckBox rateIsPercent;
    private JCheckBox rateIsTax;

    public ViewRate(Rate rate, DesktopState desktop, RefreshListener refreshListener) {

        super("View Rate", "/RTS/" + rate.getId(), false, true, false, true);
        setFrameIcon(new ImageIcon(ViewRate.class.getResource("/icons/create.png")));
        this.rate = rate;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        rateIsPercent = new JCheckBox();
        rateIsPercent.setEnabled(false);
        if (rate.isPercent()) {
            rateIsPercent.setSelected(true);
        }

        rateIsTax = new JCheckBox();
        rateIsTax.setEnabled(false);
        if (rate.isTax()) {
            rateIsTax.setSelected(true);
        }

        Form form = new Form();
        form.addInput(Elements.inputLabel("Rate ID"), new Copiable(rate.getId()));
        form.addInput(Elements.inputLabel("Name"), new Copiable(rate.getName()));
        form.addInput(Elements.inputLabel("Description"), new Copiable(rate.getDescription()));
        form.addInput(Elements.inputLabel("Percent"), rateIsPercent);
        form.addInput(Elements.inputLabel("Value"), new Copiable(String.valueOf(rate.getValue())));
        form.addInput(Elements.inputLabel("Objex"), new Copiable(rate.getObjex()));
        form.addInput(Elements.inputLabel("Reference"), new Copiable(rate.getReference()));
        form.addInput(Elements.inputLabel("Tax"), rateIsTax);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        IconButton modify = new IconButton("Modify", "modify", "Modify Rate", "/RTS/MOD");
        modify.addActionListener(_ -> {
            dispose();
            desktop.put(new ModifyRate(rate, desktop, refreshListener));
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(modify);

        IconButton delete = new IconButton("Delete", "delete", "Delete Rate", "/RTS/DEL");
        delete.addActionListener(_ -> {

        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(delete);

        toolbar.add(Elements.header("View " + rate.getName(), SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(buttons, BorderLayout.SOUTH);

        return toolbar;
    }
}