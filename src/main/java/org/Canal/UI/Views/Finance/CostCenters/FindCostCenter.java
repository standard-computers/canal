package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CCS/F
 */
public class FindCostCenter extends JInternalFrame {

    public FindCostCenter(DesktopState desktop) {
        setTitle("Find Cost Center");
        setFrameIcon(new ImageIcon(FindCostCenter.class.getResource("/icons/find.png")));
        JTextField direct = new JTextField(10);
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        Engine.router("/CCS/" + direct.getText(), desktop);
                        dispose();
                    }
                }
            }
        });
        Form f = new Form();
        f.addInput(new Label("Cost Center ID", UIManager.getColor("Label.foreground")), direct);
        add(f, BorderLayout.CENTER);
        Button find = new Button("Find");
        add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/CCS/" + direct.getText(), desktop));
                dispose();
            }
        });
        setClosable(true);
        setIconifiable(true);
    }
}