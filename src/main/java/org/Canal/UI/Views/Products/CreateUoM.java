package org.Canal.UI.Views.Products;

import org.Canal.UI.Elements.*;
import org.Canal.Utils.Includer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateUoM extends LockeState {

    public CreateUoM(Includer includer) {

        super("Create Unit of Measure", "/", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateUoM.class.getResource("/icons/windows/locke.png")));

        Form f = new Form();

        Selectable uomField = Selectables.packagingUoms();
        JTextField baseQty = Elements.input();
        Selectable baseQtyUomField = Selectables.packagingUoms();

        f.addInput(Elements.inputLabel("Unit of Measure"), uomField);
        f.addInput(Elements.inputLabel("Base Quantity"), baseQty);
        f.addInput(Elements.inputLabel("Base Quantity UOM"), baseQtyUomField);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton submit = Elements.button("Save");
        add(submit, BorderLayout.SOUTH);
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
                includer.commitUoM(uomField.getSelectedValue(), Double.parseDouble(baseQty.getText()), baseQtyUomField.getSelectedValue());
            }
        });
    }
}