package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Install extends JFrame {

    public Install(){

        setTitle("Install Canal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JButton small = Elements.button("Single Location");
        JButton medium = Elements.button("A Few Locations");
        JButton large = Elements.button("Full Enterprise");
        JButton fresh = Elements.button("Custom Install");
        JButton zip = Elements.button("From Import");
        add(small);
        add(medium);
        add(large);
        add(fresh);
        add(zip);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        small.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        medium.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        large.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        fresh.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CustomSetup();
            }
        });
        zip.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
}