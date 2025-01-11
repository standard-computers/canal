package org.Canal.UI.Views.Controllers;

import org.Canal.Start;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.CreateLocation;
import org.Canal.UI.Views.Employees.CreateEmployee;
import org.Canal.UI.Views.System.QuickExplorer;
import org.Canal.UI.Views.Users.CreateUser;
import org.Canal.Utils.Configuration;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomSetup extends JFrame {

    private Selectable themes;
    private JCheckBox showCanalCodes;

    public CustomSetup(){
        setTitle("Setup Canal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane settings = new JTabbedPane();
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
        proceed.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Configuration config = new Configuration();
                config.setShowCanalCodes(showCanalCodes.isSelected());
                config.setTheme(themes.getSelectedValue());
                Engine.setConfiguration(config);
                Pipe.saveConfiguration();
                dispose();
                QuickExplorer q = new QuickExplorer();
                q.put(new CreateLocation("/ORGS", q, null));
                q.put(new CreateEmployee(q, true));
                q.put(new CreateUser());
                Start.q = q;
            }
        });
    }

    private JPanel generalSettings(){
        JPanel panel = new JPanel();
        Form l = new Form();
        JCheckBox isClient = new JCheckBox("If yes, complete 'Server' tab");
        JTextField pkf = new JTextField("A#A#-X#AA-A#A#-AAAA-A#A#-AAAA-A#A#");
        showCanalCodes = new JCheckBox();
        themes = Selectables.themes();
        l.addInput(Elements.coloredLabel("Client CustomSetup?", Constants.colors[0]), isClient);
        l.addInput(Elements.coloredLabel("From Import?", Constants.colors[1]), Elements.link("Import *.zip",""));
        l.addInput(Elements.coloredLabel("Product Key", Constants.colors[2]), pkf);
        l.addInput(Elements.coloredLabel("Show Canal Codes?", Constants.colors[3]), showCanalCodes);
        l.addInput(Elements.coloredLabel("Theme", Constants.colors[4]), themes);
        panel.add(l);
        return panel;
    }

    private JPanel serverInformation(){
        JPanel panel = new JPanel();
        Form l = new Form();
        JTextField srvAdd = Elements.input(15);
        JTextField instanceName = Elements.input(15);
        JCheckBox isOnAWS = new JCheckBox("Provide AWS Information");
        JTextField awsAddress = Elements.input(15);
        JTextField awsUsername = Elements.input(15);
        JTextField awsKey = Elements.input(15);
        JTextField awsDir = Elements.input(15);
        l.addInput(Elements.coloredLabel("Server Address", Constants.colors[10]), srvAdd);
        l.addInput(Elements.coloredLabel("Canal Instance Name", Constants.colors[9]), instanceName);
        l.addInput(Elements.coloredLabel("Runs AWS", Constants.colors[8]), isOnAWS);
        l.addInput(Elements.coloredLabel("AWS Address", Constants.colors[7]), awsAddress);
        l.addInput(Elements.coloredLabel("AWS Username", Constants.colors[6]), awsUsername);
        l.addInput(Elements.coloredLabel("AWS Key", Constants.colors[5]), awsKey);
        l.addInput(Elements.coloredLabel("AWS Directory", Constants.colors[4]), awsDir);
        panel.add(l);
        return panel;
    }
}