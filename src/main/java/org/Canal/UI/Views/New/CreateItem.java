package org.Canal.UI.Views.New;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Singleton.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class CreateItem extends JInternalFrame {

    private Copiable orgIdField;
    private JTextField itemIdField, itemNameField, itemPriceField, itemColor, itemWidth, itemLength, itemHeight, itemWeight, tax, exciseTax, upc, uomField, packagingUnitField;
    private JCheckBox isBatched, isRentable, isSkud, isConsumable;
    private Selectable selectedVendor;

    public CreateItem(){
        setTitle("Create Item");
        Constants.checkLocke(this, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Form f1 = new Form();
        Form f2 = new Form();
        HashMap<String, String> availableVendors = new HashMap<>();
        for(Location vs : Engine.getVendors()){
            availableVendors.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        selectedVendor = new Selectable(availableVendors);
        selectedVendor.editable();
        itemIdField = new JTextField("X0" + (1000 + (Engine.getItems().size() + 1)));
        orgIdField = new Copiable(Engine.getOrganization().getId());
        itemNameField = new JTextField("Black Shirt");
        itemPriceField = new JTextField("1.00");
        isBatched = new JCheckBox(" Item expires");
        isRentable = new JCheckBox(" Item can be rented");
        isSkud = new JCheckBox(" Item has unique SKU");
        upc = new JTextField();
        uomField = new JTextField();
        packagingUnitField = new JTextField();
        f1.addInput(new Label("*Item ID", new Color(240, 240, 240)), itemIdField);
        f1.addInput(new Label("*Org ID", new Color(240, 240, 240)), orgIdField);
        f1.addInput(new Label("Item Name", Constants.colors[0]), itemNameField);
        f1.addInput(new Label("Vendor", Constants.colors[1]), selectedVendor);
        f1.addInput(new Label("Batched", Constants.colors[2]), isBatched);
        f1.addInput(new Label("Rentable", Constants.colors[3]), isRentable);
        f1.addInput(new Label("Price", Constants.colors[4]), itemPriceField);
        f1.addInput(new Label("SKU'd Product", Constants.colors[5]), isSkud);
        f1.addInput(new Label("UPC", Constants.colors[5]), upc);
        itemWidth = new JTextField("0");
        itemLength = new JTextField("0");
        itemHeight = new JTextField("0");
        itemWeight = new JTextField("0");
        tax = new JTextField("0");
        exciseTax = new JTextField("0");
        itemColor = new JTextField("Black");
        isConsumable = new JCheckBox();
        f2.addInput(new Label("Consumable?", Constants.colors[6]), isConsumable);
        f2.addInput(new Label("Color", Constants.colors[7]), itemColor);
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
        main.add(biPanel, BorderLayout.CENTER);
        main.add(actionsBar(), BorderLayout.NORTH);
        add(main);
        setIconifiable(true);
        setClosable(true);
    }

    private JPanel actionsBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton materials = new IconButton("+ Materials", "areas", "Add an area cost center");
        IconButton components = new IconButton("+ Components", "component", "Print labels for properties");
        IconButton saveitem = new IconButton("", "start", "Commit Item");
        saveitem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                commitItem();
            }
        });
        tb.add(Box.createHorizontalStrut(5));
        tb.add(materials);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(components);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(saveitem);
        return tb;
    }

    protected void commitItem() {
        Item newItem = new Item();
        newItem.setId(itemIdField.getText());
        newItem.setOrg(orgIdField.getText());
        newItem.setName(itemNameField.getText());
        newItem.setUpc(upc.getText());
        newItem.setVendor(selectedVendor.getSelectedValue());
        newItem.setColor(itemColor.getText());
        newItem.setBatched(isBatched.isSelected());
        newItem.setRentable(isRentable.isSelected());
        newItem.setSkud(isSkud.isSelected());
        newItem.setConsumable(isConsumable.isSelected());
        newItem.setPrice(Double.parseDouble(itemPriceField.getText()));
        newItem.setWidth(Double.parseDouble(itemWidth.getText()));
        newItem.setLength(Double.parseDouble(itemLength.getText()));
        newItem.setHeight(Double.parseDouble(itemHeight.getText()));
        newItem.setWeight(Double.parseDouble(itemWeight.getText()));
        newItem.setTax(Double.parseDouble(itemWeight.getText()));
        newItem.setExciseTax(Double.parseDouble(exciseTax.getText()));
        Pipe.save("/ITS", newItem);
        dispose();
        JOptionPane.showMessageDialog(this, "Item has been created");
    }
}