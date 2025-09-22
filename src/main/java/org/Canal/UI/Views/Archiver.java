package org.Canal.UI.Views;

import org.Canal.Models.Objex;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.LockeStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Archiver extends LockeState {

    public Archiver(String objex){
        super("Archiver", objex + "/ARCHV", false, true, false, true);

        JTextField objexIdField = Elements.input(20);
        Form form = new Form();
        form.addInput(Elements.coloredLabel("Objex ID", Constants.colors[0]), objexIdField);
        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        JButton confirmArchival = Elements.button("Confirm Objex Archival");
        add(confirmArchival, BorderLayout.SOUTH);
        add(Elements.header("Archive a " + objex, SwingConstants.LEFT), BorderLayout.NORTH);
        confirmArchival.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                File[] fs = Pipe.list(objex);
                for(File f : fs){
                    if(f.getName().endsWith(objex.toLowerCase().replaceAll("/", "."))){
                        Objex o = Pipe.load(f.getPath(), Objex.class);
                        if(o.getId().equals(objexIdField.getText())){
                            o.setStatus(LockeStatus.ARCHIVED);
                            //TODO
                        }
                    }
                }
                //TODO Doesn't exist
            }
        });
    }
}
