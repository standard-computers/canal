package org.Canal.UI.Views;

import com.mongodb.client.model.Filters;
import org.Canal.Models.Objex;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.ConnectDB;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.LockeStatus;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Archiver extends LockeState {

    public Archiver(String objex) {

        super("Archiver", objex + "/ARCHV", false, true, false, true);
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));

        JTextField objexIdField = Elements.input(20);
        Form form = new Form();
        form.addInput(Elements.inputLabel("Objex ID"), objexIdField);
        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        JButton confirmArchival = Elements.button("Confirm Objex Archival");
        add(confirmArchival, BorderLayout.SOUTH);
        add(Elements.header("Archive a " + objex, SwingConstants.LEFT), BorderLayout.NORTH);
        confirmArchival.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String normalized = objex.startsWith("/") ? objex.replaceFirst("/", "") : objex;
                Document match = ConnectDB.collection(normalized)
                        .find(Filters.eq("id", objexIdField.getText()))
                        .first();

                if (match != null) {
                    Objex o = Pipe.load(match, Objex.class);
                    o.setStatus(LockeStatus.ARCHIVED);
                    o.save();
                    JOptionPane.showMessageDialog(null,
                            "Objex archived successfully.",
                            "Archive Complete",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No objex found with the provided ID.",
                            "Archive Failed",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}