package org.Canal.UI.Views.Controllers;

import org.Canal.Start;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.CreateLocation;
import org.Canal.UI.Views.Employees.CreateEmployee;
import org.Canal.UI.Views.System.QuickExplorer;
import org.Canal.UI.Views.Users.CreateUser;
import org.Canal.Utils.Configuration;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;

public class CustomSetup extends JFrame {

    private Selectable themes;
    private JCheckBox showCanalCodes;

    public CustomSetup() {

        setTitle("Setup Canal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomTabbedPane settings = new CustomTabbedPane();
        settings.add(generalSettings(), "General");
        settings.add(serverInformation(), "Server");
        setLayout(new BorderLayout());
        add(settings, BorderLayout.CENTER);
        JButton proceed = Elements.button("Begin");
        add(proceed, BorderLayout.SOUTH);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        proceed.addActionListener(_ -> {

            Configuration config = new Configuration();
            config.setShowCanalCodes(showCanalCodes.isSelected());
            config.setTheme(themes.getSelectedValue());
            Engine.setConfiguration(config);
            Pipe.saveConfiguration();
            dispose();
            QuickExplorer q = new QuickExplorer();
            Start.q = q;
            q.put(new CreateLocation("/ORGS", q, null));
            q.put(new CreateEmployee(q, null));
            q.put(new CreateUser(q, null));
        });
    }

    private JPanel generalSettings() {

        JPanel panel = new JPanel();
        JCheckBox isClient = new JCheckBox("If yes, complete 'Server' tab");
        JTextField pkf = new JTextField("A#A#-X#AA-A#A#-AAAA-A#A#-AAAA-A#A#");
        showCanalCodes = new JCheckBox();
        themes = Selectables.themes();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Client CustomSetup?"), isClient);
        form.addInput(Elements.inputLabel("From Import?"), Elements.link("Import *.zip", ""));
        form.addInput(Elements.inputLabel("Product Key"), pkf);
        form.addInput(Elements.inputLabel("Show Canal Codes?"), showCanalCodes);
        form.addInput(Elements.inputLabel("Theme"), themes);
        panel.add(form);

        return panel;
    }

    private JPanel serverInformation() {

        JPanel panel = new JPanel();

        JTextField srvAdd = Elements.input(15);
        JTextField instanceName = Elements.input();
        JCheckBox isOnAWS = new JCheckBox("Provide AWS Information");
        JTextField awsAddress = Elements.input();
        JTextField awsUsername = Elements.input();
        JTextField awsKey = Elements.input();
        JTextField awsDir = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Server Address"), srvAdd);
        form.addInput(Elements.inputLabel("Canal Instance Name"), instanceName);
        form.addInput(Elements.inputLabel("Runs AWS"), isOnAWS);
        form.addInput(Elements.inputLabel("AWS Address"), awsAddress);
        form.addInput(Elements.inputLabel("AWS Username"), awsUsername);
        form.addInput(Elements.inputLabel("AWS Key"), awsKey);
        form.addInput(Elements.inputLabel("AWS Directory"), awsDir);
        panel.add(form);

        return panel;
    }
}