package org.Canal.UI.Views;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Deleter extends LockeState {

    public Deleter(String objex, RefreshListener refreshListener) {

        super("Single Objex Deleter", objex + "/DEL", false, true, false, true);
        setFrameIcon(new ImageIcon(Deleter.class.getResource("/icons/delete.png")));

        JTextField objexIdField = Elements.input(20);
        Form f = new Form();
        f.addInput(Elements.coloredLabel("Objex ID", Constants.colors[0]), objexIdField);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton confirmDeletion = Elements.button("Confirm Objex Deletion");
        add(confirmDeletion, BorderLayout.SOUTH);
        add(Elements.header("Delete a " + objex, SwingConstants.LEFT), BorderLayout.NORTH);
        confirmDeletion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(Pipe.delete(objex, objexIdField.getText())){
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                    dispose();
                    JOptionPane.showMessageDialog(null, "Objex deleted successfully");
                }else{
                    JOptionPane.showMessageDialog(null, "Objex could not be deleted");
                }
            }
        });
    }
}