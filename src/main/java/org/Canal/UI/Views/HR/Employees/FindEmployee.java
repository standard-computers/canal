package org.Canal.UI.Views.HR.Employees;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.Managers.Controller;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /EMPS/F
 */
public class FindEmployee extends JInternalFrame {

    public FindEmployee(DesktopState desktop) {
        setTitle("Find Employees");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/find.png")));
        Form f = new Form();
        JTextField empIdField = new JTextField(10);
        JTextField empNameField = new JTextField(10);
        JTextField empFindLimit = new JTextField("1", 10);
        f.addInput(new Label("Item ID", UIManager.getColor("Label.foreground")), empNameField);
        f.addInput(new Label("[or] Name", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(new Label("Hit Limit", UIManager.getColor("Label.foreground")), empFindLimit);
        JPanel main = new JPanel(new BorderLayout());
        main.add(f, BorderLayout.CENTER);
        Button find = new Button("Find");
        main.add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/EMPS/" + empIdField.getText(), desktop));
                dispose();
            }
        });
        add(main);
        setIconifiable(true);
        setClosable(true);
    }
}