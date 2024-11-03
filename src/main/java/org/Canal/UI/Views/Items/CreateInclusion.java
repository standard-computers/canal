package org.Canal.UI.Views.Items;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;

public class CreateInclusion extends JInternalFrame {

    public CreateInclusion(String title) {
        super(title, false, true, false, true);
        Form f = new Form();
        JTextField inclusionId = Elements.input();
        UOMField usage = new UOMField();
        f.addInput(new Label("Material/Component ID", Constants.colors[9]), inclusionId);
        f.addInput(new Label("Usage", Constants.colors[8]), usage);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button create = new Button("Include");
        add(create, BorderLayout.SOUTH);
    }
}