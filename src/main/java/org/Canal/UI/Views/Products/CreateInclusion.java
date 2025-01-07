package org.Canal.UI.Views.Products;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;

public class CreateInclusion extends LockeState {

    public CreateInclusion(String title) {

        super(title, "/", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateInclusion.class.getResource("/icons/create.png")));

        Form f = new Form();
        JTextField inclusionId = Elements.input();
        UOMField usage = new UOMField();
        f.addInput(Elements.coloredLabel("Material/Component ID", Constants.colors[9]), inclusionId);
        f.addInput(Elements.coloredLabel("Usage", Constants.colors[8]), usage);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Include");
        add(create, BorderLayout.SOUTH);
    }
}