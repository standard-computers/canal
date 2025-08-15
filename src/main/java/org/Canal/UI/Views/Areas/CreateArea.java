package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * /AREAS/NEW
 * Create a new Area for a location
 */
public class CreateArea extends LockeState {

    private String location;
    private RefreshListener refreshListener;
    private JTextField areaIdField;
    private Selectable availableLocations;
    private JTextField areaNameField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    public CreateArea(String location, RefreshListener refreshListener) {

        super("New Area", "/AREAS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));
        this.location = location;
        this.refreshListener = refreshListener;

        add(Elements.header("New Area", SwingConstants.LEFT), BorderLayout.NORTH);
        add(content(), BorderLayout.CENTER);
    }

    public JPanel toolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Area");
        copyFrom.addActionListener(e -> {
            String areaId = JOptionPane.showInputDialog(CreateArea.this, "Enter Area ID", "Copy Area", JOptionPane.QUESTION_MESSAGE);
            if(!areaId.isEmpty()){
                Area a = Engine.getArea(areaId);
                areaIdField.setText(a.getId());
                availableLocations.setSelectedValue(a.getLocation());
                areaNameField.setText(a.getName());
                //TODO Set UOM
                widthField.setValue(String.valueOf(a.getWidth()));
                lengthField.setValue(String.valueOf(a.getLength()));
                heightField.setValue(String.valueOf(a.getHeight()));
            }
        });
        IconButton review = new IconButton("Review", "review", "Review Area Data");
        IconButton create = new IconButton("Create", "execute", "Refresh Data");
        create.addActionListener(e -> {
            Area newArea = new Area();
            newArea.setId(areaIdField.getText().trim());
            newArea.setLocation(availableLocations.getSelectedValue());
            newArea.setName(areaNameField.getText());
            newArea.setWidth(Double.parseDouble(widthField.getValue()));
            newArea.setWidthUOM(widthField.getUOM());
            newArea.setLength(Double.parseDouble(lengthField.getValue()));
            newArea.setLengthUOM(lengthField.getUOM());
            newArea.setHeight(Double.parseDouble(heightField.getValue()));
            newArea.setHeightUOM(heightField.getUOM());
            Pipe.save("/AREAS", newArea);
            dispose();
            JOptionPane.showMessageDialog(CreateArea.this, "Area Created");
            if(refreshListener != null){
                refreshListener.refresh();
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(create);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        panel.add(tb, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());

        areaIdField = Elements.input();
        availableLocations = Selectables.allLocations();
        if(location == null){
            areaIdField.setText("A-" + Engine.getAreas().size());
        }else{
            areaIdField.setText("A-" + (Engine.getAreas(location).size() + 1) + "-" + location);
            availableLocations.setSelectedValue(location);
        }
        areaNameField = Elements.input(areaIdField.getText(), 20);
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(Elements.coloredLabel("*Location", UIManager.getColor("Label.foreground")), availableLocations);
        f.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);

        panel.add(toolbar(), BorderLayout.NORTH);
        panel.add(f, BorderLayout.CENTER);

        return panel;
    }
}