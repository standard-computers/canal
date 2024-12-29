package org.Canal.UI.Views.Products.Materials;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
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
 * /MTS/NEW
 */
public class CreateMaterial extends LockeState {

    private DesktopState desktop;
    private Selectable organizations;
    private JTextField materialIdField;
    private JTextField materialNameField;
    private JTextField materialPriceField;
    private JTextField materialColor;
    private JTextField tax;
    private JTextField exciseTax;
    private JTextField upc;
    private JTextField baseQtyField;
    private Selectable packagingUnits;
    private UOMField iniVolumeField;
    private UOMField itemWidth;
    private UOMField itemLength;
    private UOMField itemHeight;
    private UOMField itemWeight;
    private JCheckBox isBatched;
    private JCheckBox isSkud;
    private Selectable selectedVendor;

    public CreateMaterial(DesktopState desktop) {

        super("Create Material", "/MTS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateMaterial.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;

        Form f1 = new Form();
        JButton selectPhoto = Elements.button("Select Photo");
        selectedVendor = Selectables.vendors();
        selectedVendor.editable();
        materialIdField = Elements.input(((String) Engine.codex("MTS", "prefix")) + (1000 + (Engine.getItems().size() + 1)));
        organizations = Selectables.organizations();
        materialNameField = Elements.input("Black Shirt");
        materialPriceField = Elements.input("1.00");
        isBatched = new JCheckBox("Item expires");
        isSkud = new JCheckBox("Item has unique SKU");
        upc = Elements.input();
        f1.addInput(new Label("*Item ID", UIManager.getColor("Label.foreground")), materialIdField);
        f1.addInput(new Label("*Organization ID", UIManager.getColor("Label.foreground")), organizations);
        f1.addInput(new Label("Item Photo", Constants.colors[0]), selectPhoto);
        f1.addInput(new Label("Item Name", Constants.colors[1]), materialNameField);
        f1.addInput(new Label("Vendor", Constants.colors[2]), selectedVendor);
        f1.addInput(new Label("Batched", Constants.colors[3]), isBatched);
        f1.addInput(new Label("Price", Constants.colors[4]), materialPriceField);
        f1.addInput(new Label("SKU'd Product", Constants.colors[5]), isSkud);
        f1.addInput(new Label("UPC", Constants.colors[6]), upc);

        Form f2 = new Form();
        baseQtyField = Elements.input();
        packagingUnits = Selectables.packagingUoms();
        itemWidth = new UOMField();
        itemLength = new UOMField();
        itemHeight = new UOMField();
        itemWeight = new UOMField();
        tax = Elements.input();
        exciseTax = Elements.input();
        materialColor = Elements.input("Black");
        iniVolumeField = new UOMField();
        f2.addInput(new Label("Packaging Base Quantity", Constants.colors[10]), baseQtyField);
        f2.addInput(new Label("Packaging UOM", Constants.colors[9]), packagingUnits);
        f2.addInput(new Label("Ini. Volume", Constants.colors[8]), iniVolumeField);
        f2.addInput(new Label("Color", Constants.colors[7]), materialColor);
        f2.addInput(new Label("Width", Constants.colors[6]), itemWidth);
        f2.addInput(new Label("Length", Constants.colors[5]), itemLength);
        f2.addInput(new Label("Height", Constants.colors[4]), itemHeight);
        f2.addInput(new Label("Weight", Constants.colors[3]), itemWeight);
        f2.addInput(new Label("Tax (0.05 as 5%)", Constants.colors[1]), tax);
        f2.addInput(new Label("Excise Tax (0.05 as 5%)", Constants.colors[0]), exciseTax);

        JPanel biPanel = new JPanel(new GridLayout(1, 2));
        f1.setBorder(new EmptyBorder(5, 5, 5, 5));
        f2.setBorder(new EmptyBorder(5, 5, 5, 5));
        biPanel.add(f1);
        biPanel.add(f2);

        JPanel main = new JPanel(new BorderLayout());
        main.add(Elements.header("Create Material", SwingConstants.LEFT), BorderLayout.NORTH);
        main.add(biPanel, BorderLayout.CENTER);
        main.add(actionsBar(), BorderLayout.SOUTH);

        add(main);
    }

    private JPanel actionsBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton saveitem = new IconButton("Create Material", "start", "Commit Material");
        saveitem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                createMaterial();
            }
        });
        tb.add(review);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        tb.add(saveitem);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    protected void createMaterial() {
        Item material = new Item();
        material.setId(materialIdField.getText());
        material.setOrg(organizations.getSelectedValue());
        material.setName(materialNameField.getText());
        material.setUpc(upc.getText());
        material.setVendor(selectedVendor.getSelectedValue());
        material.setColor(materialColor.getText());
        material.setBatched(isBatched.isSelected());
        material.setSkud(isSkud.isSelected());
        material.setPrice(Double.parseDouble(materialPriceField.getText()));
        material.setWidth(Double.parseDouble(itemWidth.getValue()));
        material.setWidthUOM(itemWidth.getUOM());
        material.setLength(Double.parseDouble(itemLength.getValue()));
        material.setLengthUOM(itemLength.getUOM());
        material.setHeight(Double.parseDouble(itemHeight.getValue()));
        material.setHeightUOM(itemHeight.getUOM());
        material.setWeight(Double.parseDouble(itemWeight.getValue()));
        material.setWeightUOM(itemWeight.getUOM());
        material.setTax(Double.parseDouble(itemWeight.getValue()));
        material.setExciseTax(Double.parseDouble(exciseTax.getText()));
        Pipe.save("/MTS", material);
        dispose();
        JOptionPane.showMessageDialog(this, "Material has been created");
        desktop.put(new MaterialView(material));
    }
}