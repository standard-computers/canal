package org.Canal.UI.Views.Productivity.Tasks;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /MVMT/TSKS/F
 */
public class FindTask extends JInternalFrame {

    public FindTask(DesktopState desktop) {
        setTitle("Find Task");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/find.png")));
        Form f = new Form();
        JTextField empIdField = new JTextField(10);
        JTextField empNameField = new JTextField(10);
        JTextField empFindLimit = new JTextField("1", 10);
        f.addInput(new Label("User ID", UIManager.getColor("Label.foreground")), empNameField);
        f.addInput(new Label("[or] Name", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(new Label("[or] User ID", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(new Label("Hit Limit", UIManager.getColor("Label.foreground")), empFindLimit);
        JPanel main = new JPanel(new BorderLayout());
        main.add(f, BorderLayout.CENTER);
        Button find = new Button("Find");
        main.add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/MVMT/TSKS/" + empIdField.getText(), desktop));
                dispose();
            }
        });
        add(main);
        setIconifiable(true);
        setClosable(true);
    }
}