package org.Canal.UI.Views.Products.Components;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CMPS/NEW
 */
public class CreateComponent extends LockeState {

    private DesktopState desktop;
    private Selectable organizations;
    private JTextField materialIdField;
    private JTextField materialNameField;
    private JTextField materialPriceField;
    private JTextField materialColor;
    private JTextField baseQtyField;
    private Selectable packagingUnits;
    private UOMField itemWidth;
    private UOMField itemLength;
    private UOMField itemHeight;
    private UOMField itemWeight;
    private JTextField tax;
    private JTextField exciseTax;
    private JTextField upc;
    private JCheckBox isBatched, isSkud;
    private Selectable selectedVendor;

    public CreateComponent(DesktopState desktop){

        super("Create Component", "/", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateComponent.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;

        Form f1 = new Form();
        JButton selectPhoto = Elements.button("Select Photo");
        selectedVendor = Selectables.vendors();
        selectedVendor.editable();
        materialIdField = Elements.input("XI0" + (1000 + (Engine.products.getComponents().size() + 1)));
        organizations = Selectables.organizations();
        materialNameField = Elements.input("Black Shirt");
        materialPriceField = Elements.input("1.00");
        isBatched = new JCheckBox("Component expires");
        isSkud = new JCheckBox("Component has unique SKU");
        upc = Elements.input();
        f1.addInput(Elements.coloredLabel("*Component ID", new Color(240, 240, 240)), materialIdField);
        f1.addInput(Elements.coloredLabel("*Organization ID", new Color(240, 240, 240)), organizations);
        f1.addInput(Elements.coloredLabel("Component Photo", Constants.colors[0]), selectPhoto);
        f1.addInput(Elements.coloredLabel("Component Name", Constants.colors[1]), materialNameField);
        f1.addInput(Elements.coloredLabel("Vendor", Constants.colors[2]), selectedVendor);
        f1.addInput(Elements.coloredLabel("Batched", Constants.colors[3]), isBatched);
        f1.addInput(Elements.coloredLabel("Price", Constants.colors[4]), materialPriceField);
        f1.addInput(Elements.coloredLabel("SKU'd Product", Constants.colors[5]), isSkud);
        f1.addInput(Elements.coloredLabel("UPC", Constants.colors[6]), upc);

        Form f2 = new Form();
        baseQtyField = Elements.input();
        packagingUnits = Selectables.packagingUoms();
        itemWidth = new UOMField();
        itemLength = new UOMField();
        itemHeight = new UOMField();
        itemWeight = new UOMField();
        tax = Elements.input("0");
        exciseTax = Elements.input("0");
        materialColor = Elements.input("Black");
        f2.addInput(Elements.coloredLabel("Packaging Base Quantity", Constants.colors[10]), baseQtyField);
        f2.addInput(Elements.coloredLabel("Packaging UOM", Constants.colors[9]), packagingUnits);
        f2.addInput(Elements.coloredLabel("Color", Constants.colors[8]), materialColor);
        f2.addInput(Elements.coloredLabel("Width", Constants.colors[7]), itemWidth);
        f2.addInput(Elements.coloredLabel("Length", Constants.colors[6]), itemLength);
        f2.addInput(Elements.coloredLabel("Height", Constants.colors[5]), itemHeight);
        f2.addInput(Elements.coloredLabel("Weight", Constants.colors[4]), itemWeight);
        f2.addInput(Elements.coloredLabel("Tax (0.05 as 5%)", Constants.colors[3]), tax);
        f2.addInput(Elements.coloredLabel("Excise Tax (0.05 as 5%)", Constants.colors[2]), exciseTax);

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
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton saveitem = new IconButton("Create Component", "start", "Commit Item");
        saveitem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                createComponent();
            }
        });
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(saveitem);
        tb.add(Box.createHorizontalStrut(5));
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    protected void createComponent() {
        Item component = new Item();
        component.setId(materialIdField.getText());
        component.setOrg(organizations.getSelectedValue());
        component.setName(materialNameField.getText());
        component.setUpc(upc.getText());
        component.setVendor(selectedVendor.getSelectedValue());
        component.setColor(materialColor.getText());
        component.setBatched(isBatched.isSelected());
        component.setSkud(isSkud.isSelected());
        component.setPrice(Double.parseDouble(materialPriceField.getText()));
        component.setWidth(Double.parseDouble(itemWidth.getValue()));
        component.setLength(Double.parseDouble(itemLength.getValue()));
        component.setHeight(Double.parseDouble(itemHeight.getValue()));
        component.setWeight(Double.parseDouble(itemWeight.getValue()));
        component.setTax(Double.parseDouble(itemWeight.getValue()));
        component.setExciseTax(Double.parseDouble(exciseTax.getText()));
        Pipe.save("/CMPS", component);
        dispose();
        JOptionPane.showMessageDialog(this, "Component has been created");
        desktop.put(new ViewComponent(component));
    }
}