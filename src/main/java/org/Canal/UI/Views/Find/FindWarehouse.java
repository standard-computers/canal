package org.Canal.UI.Views.Find;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.Singleton.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FindWarehouse extends JInternalFrame {

    public FindWarehouse(DesktopState desktop) {
        setTitle("Find Warehouse");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/find.png")));
        Form f = new Form();
        JTextField direct = new JTextField(10);
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        Engine.router("/WHS/" + direct.getText(), desktop);
                        dispose();
                    }
                }
            }
        });
        f.addInput(new Label("Warehouse ID", UIManager.getColor("Label.foreground")), direct);
        JPanel main = new JPanel(new BorderLayout());
        main.add(f, BorderLayout.CENTER);
        Button find = new Button("Open");
        main.add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/WHS/" + direct.getText(), desktop));
                dispose();
            }
        });
        add(main);
        setIconifiable(true);
        setClosable(true);
    }
}