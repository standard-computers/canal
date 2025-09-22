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
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
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
        form.addInput(Elements.coloredLabel("*New Rate ID", UIManager.getColor("Label.foreground")), rateId);
        form.addInput(Elements.coloredLabel("Name", Constants.colors[10]), rateName);
        form.addInput(Elements.coloredLabel("Description", Constants.colors[9]), rateDescription);
        form.addInput(Elements.coloredLabel("Percent", Constants.colors[8]), rateIsPercent);
        form.addInput(Elements.coloredLabel("Value", Constants.colors[7]), rateValue);
        form.addInput(Elements.coloredLabel("Objex", Constants.colors[6]), objexes);
        form.addInput(Elements.coloredLabel("Reference", Constants.colors[5]), rateReference);
        form.addInput(Elements.coloredLabel("Tax", Constants.colors[4]), rateIsTax);

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
            String prId = JOptionPane.showInputDialog(CreateRate.this, "Enter Rate ID");
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
}