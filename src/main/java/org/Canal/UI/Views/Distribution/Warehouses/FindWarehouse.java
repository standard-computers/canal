package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /WHS/F
 */
public class FindWarehouse extends JInternalFrame {

    public FindWarehouse(DesktopState desktop) {
        super("Find Warehouse", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/warehouses.png")));
        Form f = new Form();
        JTextField direct = Elements.input(10);
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/WHS/" + direct.getText(), desktop));
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
    }
}