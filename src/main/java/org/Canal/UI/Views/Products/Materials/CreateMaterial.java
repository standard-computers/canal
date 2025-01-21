package org.Canal.UI.Views.Products.Materials;

import org.Canal.Models.SupplyChainUnits.OrderLineItem;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
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
        materialIdField = Elements.input(((String) Engine.codex("MTS", "prefix")) + (1000 + (Engine.products.getMaterials().size() + 1)));
        organizations = Selectables.organizations();
        materialNameField = Elements.input("Black Shirt");
        materialPriceField = Elements.input("1.00");
        isBatched = new JCheckBox("Material expires");
        isSkud = new JCheckBox("Material has unique SKU");
        upc = Elements.input();
        f1.addInput(Elements.coloredLabel("*New Material ID", UIManager.getColor("Label.foreground")), materialIdField);
        f1.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), organizations);
        f1.addInput(Elements.coloredLabel("Material Photo", Constants.colors[0]), selectPhoto);
        f1.addInput(Elements.coloredLabel("Material Name", Constants.colors[1]), materialNameField);
        f1.addInput(Elements.coloredLabel("Vendor", Constants.colors[2]), selectedVendor);
        f1.addInput(Elements.coloredLabel("Batched", Constants.colors[3]), isBatched);
        f1.addInput(Elements.coloredLabel("Price", Constants.colors[4]), materialPriceField);
        f1.addInput(Elements.coloredLabel("SKU'd Product", Constants.colors[5]), isSkud);
        f1.addInput(Elements.coloredLabel("UPC", Constants.colors[6]), upc);

        Form f2 = new Form();
        baseQtyField = Elements.input("1");
        packagingUnits = Selectables.packagingUoms();
        iniVolumeField = new UOMField();
        materialColor = Elements.input("Black");
        itemWidth = new UOMField();
        itemLength = new UOMField();
        itemHeight = new UOMField();
        itemWeight = new UOMField();
        tax = Elements.input("0");
        exciseTax = Elements.input("0");
        f2.addInput(Elements.coloredLabel("Packaging Base Quantity", Constants.colors[10]), baseQtyField);
        f2.addInput(Elements.coloredLabel("Packaging UOM", Constants.colors[9]), packagingUnits);
        f2.addInput(Elements.coloredLabel("Ini. Volume", Constants.colors[8]), iniVolumeField);
        f2.addInput(Elements.coloredLabel("Color", Constants.colors[7]), materialColor);
        f2.addInput(Elements.coloredLabel("Width", Constants.colors[6]), itemWidth);
        f2.addInput(Elements.coloredLabel("Length", Constants.colors[5]), itemLength);
        f2.addInput(Elements.coloredLabel("Height", Constants.colors[4]), itemHeight);
        f2.addInput(Elements.coloredLabel("Weight", Constants.colors[3]), itemWeight);
        f2.addInput(Elements.coloredLabel("Tax (0.05 as 5%)", Constants.colors[1]), tax);
        f2.addInput(Elements.coloredLabel("Excise Tax (0.05 as 5%)", Constants.colors[0]), exciseTax);

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
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        tb.add(review);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        tb.add(saveitem);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    protected void createMaterial() {
        OrderLineItem material = new OrderLineItem();
        material.setId(materialIdField.getText());
        material.setOrg(organizations.getSelectedValue());
        material.setName(materialNameField.getText());
        material.setUpc(upc.getText());
        material.setVendor(selectedVendor.getSelectedValue());
        material.setColor(materialColor.getText());
        material.setBatched(isBatched.isSelected());
        material.setSkud(isSkud.isSelected());
        material.setPrice(Double.parseDouble(materialPriceField.getText()));
        material.setBaseQuantity(Double.parseDouble(baseQtyField.getText()));
        material.setPackagingUnit(packagingUnits.getSelectedValue());
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
        desktop.put(new ViewMaterial(material));
    }
}