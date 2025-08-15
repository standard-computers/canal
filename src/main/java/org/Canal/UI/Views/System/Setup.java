package org.Canal.UI.Views.System;

import org.Canal.Start;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Views.Controllers.CustomSetup;
import org.Canal.Utils.Codex;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Json;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This JFrame present setup options to make Canal Setup easier :)
 */
public class Setup extends JFrame {

    public Setup(){

        setTitle("Setup Ratio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        if(Engine.codex == null){
            Codex cdx = new Codex();
            Engine.codex = cdx;
            Json.save(Start.DIR + "\\codex.cdx", cdx);
        }

        JButton small = Elements.button("Single Location");
        JButton medium = Elements.button("A Few Locations");
        JButton large = Elements.button("Full Enterprise");
        JButton custom = Elements.button("Custom Setup");
        JButton fromImport = Elements.button("From Import");
        add(small);
        add(medium);
        add(large);
        add(custom);
        add(fromImport);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        small.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                dispose();
                Pipe.saveConfiguration();
                new IniSystem();
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
        custom.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CustomSetup();
            }
        });
        fromImport.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
}