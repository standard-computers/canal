package org.Canal.UI.Views.Components;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Material;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /CMPS/NEW
 */
public class CreateComponent extends LockeState {

    private Copiable orgIdField;
    private JTextField materialIdField;
    private JTextField materialNameField;
    private JTextField materialPriceField;
    private JTextField materialColor;
    private UOMField itemWidth;
    private UOMField itemLength;
    private UOMField itemHeight;
    private UOMField itemWeight;
    private JTextField tax;
    private JTextField exciseTax;
    private JTextField upc;
    private JCheckBox isBatched, isSkud;
    private Selectable selectedVendor;

    public CreateComponent(){
        super("Create Component", "/", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Form f1 = new Form();
        Form f2 = new Form();
        HashMap<String, String> availableVendors = new HashMap<>();
        for(Location vs : Engine.getLocations("VEND")){
            availableVendors.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        selectedVendor = new Selectable(availableVendors);
        selectedVendor.editable();
        materialIdField = new JTextField("XI0" + (1000 + (Engine.getItems().size() + 1)));
        orgIdField = new Copiable(Engine.getOrganization().getId());
        materialNameField = new JTextField("Black Shirt");
        materialPriceField = new JTextField("1.00");
        isBatched = new JCheckBox("Item expires");
        isSkud = new JCheckBox("Item has unique SKU");
        upc = new JTextField();
        f1.addInput(new Label("*Component ID", new Color(240, 240, 240)), materialIdField);
        f1.addInput(new Label("*Org ID", new Color(240, 240, 240)), orgIdField);
        f1.addInput(new Label("Component Name", Constants.colors[0]), materialNameField);
        f1.addInput(new Label("Vendor", Constants.colors[1]), selectedVendor);
        f1.addInput(new Label("Batched", Constants.colors[2]), isBatched);
        f1.addInput(new Label("Price", Constants.colors[4]), materialPriceField);
        f1.addInput(new Label("SKU'd Product", Constants.colors[5]), isSkud);
        f1.addInput(new Label("UPC", Constants.colors[5]), upc);
        itemWidth = new UOMField();
        itemLength = new UOMField();
        itemHeight = new UOMField();
        itemWeight = new UOMField();
        tax = new JTextField("0");
        exciseTax = new JTextField("0");
        materialColor = new JTextField("Black");
        f2.addInput(new Label("Color", Constants.colors[7]), materialColor);
        f2.addInput(new Label("Width", Constants.colors[8]), itemWidth);
        f2.addInput(new Label("Length", Constants.colors[9]), itemLength);
        f2.addInput(new Label("Height", Constants.colors[10]), itemHeight);
        f2.addInput(new Label("Weight", Constants.colors[1]), itemWeight);
        f2.addInput(new Label("Tax", Constants.colors[1]), tax);
        f2.addInput(new Label("Excise Tax", Constants.colors[1]), exciseTax);
        JPanel biPanel = new JPanel(new GridLayout(1, 2));
        f1.setBorder(new EmptyBorder(5, 5, 5, 5));
        f2.setBorder(new EmptyBorder(5, 5, 5, 5));
        biPanel.add(f1);
        biPanel.add(f2);
        JPanel main = new JPanel(new BorderLayout());
        main.add(Elements.header("Create Component", SwingConstants.LEFT), BorderLayout.NORTH);
        main.add(biPanel, BorderLayout.CENTER);
        main.add(actionsBar(), BorderLayout.SOUTH);
        add(main);
    }

    private JPanel actionsBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton saveitem = new IconButton("", "start", "Commit Item");
        saveitem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                commitItem();
            }
        });
        tb.add(Box.createHorizontalStrut(5));
        tb.add(saveitem);
        return tb;
    }

    protected void commitItem() {
        Material newItem = new Material();
        newItem.setId(materialIdField.getText());
        newItem.setOrg(orgIdField.getText());
        newItem.setName(materialNameField.getText());
        newItem.setUpc(upc.getText());
        newItem.setVendor(selectedVendor.getSelectedValue());
        newItem.setColor(materialColor.getText());
        newItem.setBatched(isBatched.isSelected());
        newItem.setSkud(isSkud.isSelected());
        newItem.setPrice(Double.parseDouble(materialPriceField.getText()));
        newItem.setWidth(Double.parseDouble(itemWidth.getValue()));
        newItem.setLength(Double.parseDouble(itemLength.getValue()));
        newItem.setHeight(Double.parseDouble(itemHeight.getValue()));
        newItem.setWeight(Double.parseDouble(itemWeight.getValue()));
        newItem.setTax(Double.parseDouble(itemWeight.getValue()));
        newItem.setExciseTax(Double.parseDouble(exciseTax.getText()));
        Pipe.save("/MTS", newItem);
        dispose();
        JOptionPane.showMessageDialog(this, "Material has been created");
    }
}