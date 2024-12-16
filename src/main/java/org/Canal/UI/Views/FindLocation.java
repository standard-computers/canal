package org.Canal.UI.Views;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /$/F
 */
public class FindLocation extends LockeState {

    public FindLocation(String objexType, DesktopState desktop) {
        super("Find Location", objexType + "/F", false, true, false, true);
        setFrameIcon(new ImageIcon(FindLocation.class.getResource("/icons/find.png")));
        Form f = new Form();
        Selectable objex = Selectables.locationObjex(objexType);
        JTextField locId = Elements.input(15);
        JTextField locName = Elements.input(15);
        JTextField locationAddressField = Elements.input(15);
        f.addInput(new Label("Location Type", Constants.colors[0]), objex);
        f.addInput(new Label("Location ID", Constants.colors[1]), locId);
        f.addInput(new Label("Location Name", Constants.colors[2]), locName);
        f.addInput(new Label("Location Address", Constants.colors[3]), locationAddressField);
        add(f, BorderLayout.CENTER);
        JButton find = Elements.button("Perform Find");
        add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {


                dispose();
            }
        });
    }
}