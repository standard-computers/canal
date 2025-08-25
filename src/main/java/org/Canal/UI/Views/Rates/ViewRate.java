package org.Canal.UI.Views.Rates;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.BusinessUnits.Rate;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
        if(rate.isPercent()){
            rateIsPercent.setSelected(true);
        }

        rateIsTax = new JCheckBox();
        rateIsTax.setEnabled(false);
        if(rate.isTax()){
            rateIsTax.setSelected(true);
        }

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Rate ID", UIManager.getColor("Label.foreground")), new Copiable(rate.getId()));
        f.addInput(Elements.coloredLabel("Name", Constants.colors[10]), new Copiable(rate.getName()));
        f.addInput(Elements.coloredLabel("Description", Constants.colors[8]), new Copiable(rate.getDescription()));
        f.addInput(Elements.coloredLabel("Percent", Constants.colors[7]), rateIsPercent);
        f.addInput(Elements.coloredLabel("Value", Constants.colors[6]), new Copiable(String.valueOf(rate.getValue())));
        f.addInput(Elements.coloredLabel("Objex", Constants.colors[5]), new Copiable(rate.getObjex()));
        f.addInput(Elements.coloredLabel("Reference", Constants.colors[9]), new Copiable(rate.getReference()));
        f.addInput(Elements.coloredLabel("Tax", Constants.colors[4]), rateIsTax);

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
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
