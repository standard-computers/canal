package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.LockeState;
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
 * Find Warehouse with Warehouse ID
 * Opens /WHS/$[WWAREHOUSE_ID], Warehouse Controller
 */
public class FindWarehouse extends LockeState {

    public FindWarehouse(DesktopState desktop) {
        super("Find Warehouse", "/WHS/F", false, true, false, true);
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
        JButton find = Elements.button("Open");
        main.add(find, BorderLayout.SOUTH);
        add(main);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/WHS/" + direct.getText(), desktop));
                dispose();
            }
        });
    }
}