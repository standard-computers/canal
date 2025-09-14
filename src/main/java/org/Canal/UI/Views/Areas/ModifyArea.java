package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /AREAS/MOD/$
 * Modify an Area
 */
public class ModifyArea extends LockeState {

    private Area area;
    private RefreshListener refreshListener;
    private JTextField availableLocations;
    private JTextField areaNameField;
    private Selectable statuses;

    //Controls
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsPurchasing;
    private JCheckBox allowsSales;

    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    public ModifyArea(Area area, RefreshListener refreshListener) {

        super("Modify " + area.getId(), "/AREAS/MOD/" + area.getId(), false, true, false, true);
        setFrameIcon(new ImageIcon(ModifyArea.class.getResource("/icons/windows/locke.png")));
        this.area = area;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("General", general());
        tabbedPane.addTab("Controls", controls());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Modify " + area.getName() + " - " + area.getId(), SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save Changes");
        save.addActionListener(_ -> {

            area.setName(areaNameField.getText());
            area.setStatus(LockeStatus.valueOf(statuses.getSelectedValue()));
            area.setWidth(Double.parseDouble(widthField.getValue()));
            area.setWidthUOM(widthField.getUOM());
            area.setLength(Double.parseDouble(lengthField.getValue()));
            area.setLengthUOM(lengthField.getUOM());
            area.setHeight(Double.parseDouble(heightField.getValue()));
            area.setHeightUOM(heightField.getUOM());
            area.setAllowsInventory(allowsInventory.isSelected());
            area.setAllowsProduction(allowsProduction.isSelected());
            area.setAllowsPurchasing(allowsPurchasing.isSelected());
            area.setAllowsSales(allowsSales.isSelected());
            area.save();

            dispose();
            if (refreshListener != null) {
                refreshListener.refresh();
            }
        });
        tb.add(save);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        panel.add(tb, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel general() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        availableLocations = Elements.input(area.getLocation());
        areaNameField = Elements.input(area.getName(), 20);

        statuses = Selectables.statusTypes();
        statuses.setSelectedValue(String.valueOf(area.getStatus()));

        widthField = new UOMField();
        widthField.setValue(String.valueOf(area.getWidth()));
        widthField.setUOM(area.getWidthUOM());

        lengthField = new UOMField();
        lengthField.setValue(String.valueOf(area.getLength()));
        lengthField.setUOM(area.getLengthUOM());

        heightField = new UOMField();
        heightField.setValue(String.valueOf(area.getHeight()));
        heightField.setUOM(area.getHeightUOM());

        Form f = new Form();
        f.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(area.getId()));
        f.addInput(Elements.coloredLabel("Location", UIManager.getColor("Label.foreground")), availableLocations);
        f.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(Elements.coloredLabel("Status", Constants.colors[10]), statuses);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        panel.add(f);

        return panel;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form f = new Form();

        allowsInventory = new JCheckBox("Inventory Movements", area.allowsInventory());
        allowsProduction = new JCheckBox("Allows Production", area.allowsProduction());
        allowsSales = new JCheckBox("Sales Order Processing", area.allowsSales());
        allowsPurchasing = new JCheckBox("Purchase Order Processing", area.allowsPurchasing());

        f.addInput(Elements.coloredLabel("Allow Inventory", Constants.colors[0]), allowsInventory);
        f.addInput(Elements.coloredLabel("Allow Production", Constants.colors[1]), allowsProduction);
        f.addInput(Elements.coloredLabel("Allow Sales", Constants.colors[2]), allowsSales);
        f.addInput(Elements.coloredLabel("Allow Purchasing", Constants.colors[3]), allowsPurchasing);
        controls.add(f);

        return controls;
    }
}