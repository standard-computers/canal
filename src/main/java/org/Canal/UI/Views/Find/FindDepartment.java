package org.Canal.UI.Views.Find;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.Singleton.Controller;
import org.Canal.Utils.DesktopState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /DPTS/F
 */
public class FindDepartment extends JInternalFrame {

    public FindDepartment(DesktopState desktop) {
        setTitle("Find Department");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/find.png")));
        JTextField direct = new JTextField(10);
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        //TODO Logic
                        dispose();
                    }
                }
            }
        });
        Form f = new Form();
        f.addInput(new Label("Department Name [or] ID", UIManager.getColor("Label.foreground")), direct);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button find = new Button("Find");
        add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //TODO Logic
                dispose();
            }
        });
        setClosable(true);
    }
}