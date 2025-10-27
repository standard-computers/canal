package org.Canal.UI.Views.System;

import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.IconButton;
import org.Canal.Utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EnterpriseSetup extends JFrame {

    private JTextField serverURI;
    private JTextField serverUsername;
    private JTextField serverPassword;
    private JTextField mongoDbURI;
    private JTextField mongoDBUser;
    private JTextField mongoDBPassword;

    public EnterpriseSetup() {

        setTitle("Canal Enterprise Setup");
        setLayout(new BorderLayout());

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Help", help());
        tabs.addTab("Config", config());
        tabs.addTab("Server", server());
        tabs.addTab("Database", mongodb());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton testConnection = new IconButton("Test", "start", "Test Connection");
        tb.add(testConnection);
        tb.add(Box.createHorizontalStrut(5));

        IconButton start = new IconButton("Start", "execute", "Setup Canal");
        tb.add(start);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        toolbar.add(Elements.header("Setup Canal for Enterprise", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel help() {

        JPanel help = new JPanel(new BorderLayout());
        JTextArea helpText = new JTextArea();

        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        help.setBorder(new EmptyBorder(5, 5, 5, 5));

        helpText.append("Config is for the local user.\n\n");
        helpText.append("Server info is not required, UI can directly engage with MongoDB.\n\n");
        helpText.append("If no server, Codex is saved, modified, and applied locally.\n\n");
        helpText.append("If server, Codex is imported and applied.\n\n");
        helpText.append("If MongoDB is not setup or no org, then you will have to create those in suceeding steps.\n\n");

        help.add(new JScrollPane(helpText), BorderLayout.CENTER);
        return help;
    }

    private JPanel config() {
        JPanel config = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return config;
    }

    private JPanel server() {

        JPanel server = new JPanel(new FlowLayout(FlowLayout.LEFT));

        serverURI = Elements.input(20);
        serverUsername = Elements.input();
        serverPassword = Elements.input();

        Form f = new Form();
        f.addInput(Elements.inputLabel("URI"), serverURI);
        f.addInput(Elements.inputLabel("Username"), serverUsername);
        f.addInput(Elements.inputLabel("Password"), serverPassword);

        server.add(f);

        return server;
    }

    private JPanel mongodb() {

        JPanel mongodb = new JPanel(new FlowLayout(FlowLayout.LEFT));

        mongoDbURI = Elements.input(20);
        mongoDBUser = Elements.input();
        mongoDBPassword = Elements.input();

        Form f = new Form();
        f.addInput(Elements.inputLabel("URI"), mongoDbURI);
        f.addInput(Elements.inputLabel("Username"), mongoDBUser);
        f.addInput(Elements.inputLabel("Password"), mongoDBPassword);

        mongodb.add(f);

        return mongodb;
    }
}
