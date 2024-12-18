package org.Canal.UI.Views.Bins;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /BNS/DEL
 */
public class RemoveBin extends LockeState {

    public RemoveBin() {
        super("Remove Bin", "/BNS/DEL", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeBins.class.getResource("/icons/delete.png")));
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
        Form f = new Form();
        JTextField binIdField = Elements.input("", 15);
        f.addInput(new Label("Bin ID", Constants.colors[0]), binIdField);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton remove = Elements.button("Remove");
        add(remove, BorderLayout.SOUTH);
        add(Elements.header("", SwingConstants.LEFT), BorderLayout.NORTH);
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            }
        });
    }
}