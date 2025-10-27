package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Deleter extends LockeState {

    public Deleter(String objex, RefreshListener refreshListener) {

        super("Single Objex Deleter", objex + "/DEL", false, true, false, true);
        setFrameIcon(new ImageIcon(Deleter.class.getResource("/icons/delete.png")));
        setBorder(BorderFactory.createLineBorder(Color.RED, 1));

        JTextField objexIdField = Elements.input(15);
        Form form = new Form();
        form.addInput(Elements.inputLabel("Objex ID"), objexIdField);

        setLayout(new BorderLayout());
        add(Elements.header("Delete a " + objex, SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        JButton confirmDeletion = Elements.button("Confirm Objex Deletion");
        add(confirmDeletion, BorderLayout.SOUTH);

        ActionListener doDelete = _ -> {
            if (Pipe.delete(objex, objexIdField.getText())) {
                if (refreshListener != null) refreshListener.refresh();

                //If location, delete Areas and Bins as well
                if (objex.equals("/ORGS") || objex.equals("/CCS") || objex.equals("/DCSS") || objex.equals("/VEND") || objex.equals("/OFFS") || objex.equals("/WHS") || objex.equals("/TRANS/CRRS") || objex.equals("/CSTS")) {
                    for (Area a1 : Engine.getAreas(objexIdField.getText().trim())) {
                        System.out.println("Found and deleting area:" + a1.toString());
                        for (Bin b : Engine.getBinsForArea(a1.getId())) {
                            System.out.println("Found and deleting area:" + b.toString());
                            Pipe.delete("/BNS", b.getId());
                        }
                        Pipe.delete("/AREAS", a1.getId());
                    }
                }

                dispose();
                JOptionPane.showMessageDialog(this, "Objex deleted successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Objex could not be deleted");
            }
        };

        confirmDeletion.addActionListener(doDelete);
        objexIdField.addActionListener(doDelete);
        SwingUtilities.invokeLater(objexIdField::requestFocusInWindow);
    }
}