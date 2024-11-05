package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
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
 * /CATS/F
 */
public class FindCatalog extends JInternalFrame {

    public FindCatalog(DesktopState desktop) {
        super("Find Catalog", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/catalogs.png")));
        Form f = new Form();
        JTextField direct = new JTextField(10);
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        Engine.router("/CATS/" + direct.getText(), desktop);
                        dispose();
                    }
                }
            }
        });
        f.addInput(new Label("Catalog Name [or] ID", UIManager.getColor("Label.foreground")), direct);
        JPanel main = new JPanel(new BorderLayout());
        main.add(f, BorderLayout.CENTER);
        Button find = new Button("Open");
        main.add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/CATS/" + direct.getText(), desktop));
                dispose();
            }
        });
        add(main);
    }
}