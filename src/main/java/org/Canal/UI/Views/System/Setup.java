package org.Canal.UI.Views.System;

import org.Canal.Start;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Views.Controllers.CustomSetup;
import org.Canal.Utils.Codex;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This JFrame present setup options to make Canal Setup easier :)
 */
public class Setup extends JFrame {

    public Setup() {

        setTitle("Setup Canal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        if (Engine.codex == null) {
            Codex cdx = new Codex();
            Engine.codex = cdx;
            Pipe.export(Start.DIR + "\\codex.cdx", cdx);
        }

        JButton small = Elements.button("Single Location");
        small.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                dispose();
                Pipe.saveConfiguration();
                new IniSystem();
            }
        });
        add(small);

        JButton medium = Elements.button("A Few Locations");
        medium.addActionListener(_ -> {

        });
        add(medium);

        JButton enterprise = Elements.button("Enterprise (Server and MongoDB)");
        enterprise.addActionListener(_ -> {
            dispose();
            new EnterpriseSetup();
        });
        add(enterprise);

        JButton custom = Elements.button("Custom Setup");
        custom.addActionListener(_ -> {
            dispose();
            new CustomSetup();
        });
        add(custom);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}