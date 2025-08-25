package org.Canal.UI.Views.Rates;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.BusinessUnits.Rate;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * /RTS/NEW
 */
public class ModifyRate extends LockeState {

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

    public ModifyRate(Rate rate, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify Rate", "/RTS/MOD/" + rate.getId(), false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/modify.png")));
        setLayout(new GridBagLayout());
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        rateId = Elements.input(rate.getId());
        rateName = Elements.input(rate.getName());
        rateDescription = Elements.input(rate.getDescription());
        rateIsPercent = new JCheckBox("", rate.isPercent());
        rateValue = Elements.input(String.valueOf(rate.getValue()));
        objexes = Selectables.objexForRates();
        objexes.setSelectedValue(rate.getObjex());
        rateReference = Elements.input(rate.getReference());
        rateIsTax = new JCheckBox("", rate.isTax());

        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New Rate ID", UIManager.getColor("Label.foreground")), rateId);
        f.addInput(Elements.coloredLabel("Name", Constants.colors[10]), rateName);
        f.addInput(Elements.coloredLabel("Description", Constants.colors[9]), rateDescription);
        f.addInput(Elements.coloredLabel("Percent", Constants.colors[8]), rateIsPercent);
        f.addInput(Elements.coloredLabel("Value", Constants.colors[7]), rateValue);
        f.addInput(Elements.coloredLabel("Objex", Constants.colors[6]), objexes);
        f.addInput(Elements.coloredLabel("Reference", Constants.colors[5]), rateReference);
        f.addInput(Elements.coloredLabel("Tax", Constants.colors[4]), rateIsTax);

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Rate");
        copyFrom.addActionListener(_ -> {
            String prId = JOptionPane.showInputDialog(ModifyRate.this, "Enter Rate ID");
            PurchaseRequisition pr = Engine.getPurchaseRequisition(prId);

        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(copyFrom);

        IconButton review = new IconButton("Review", "review", "Review Rate");
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


            if(refreshListener != null) {
                refreshListener.refresh();
            }
            dispose();

            if((boolean) Engine.codex.getValue("RTS", "auto_open_new")){

                desktop.put(new ViewRate(newRate, desktop, refreshListener));
            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(execute);

        toolbar.add(buttons, BorderLayout.SOUTH);
        toolbar.add(Elements.header("Create a Rate", SwingConstants.LEFT), BorderLayout.NORTH);

        return toolbar;
    }
}