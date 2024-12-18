package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.LockeState;
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
 * /ORDS/PO/F
 */
public class FindPurchaseOrder extends LockeState {

    public FindPurchaseOrder(DesktopState desktop) {
        super("Find Purchase Order", "/ORDS/PO/F", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/find.png")));
        Form f = new Form();
        JTextField direct = Elements.input(15);
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/ORDS/" + direct.getText(), desktop));
                        dispose();
                    }
                }
            }
        });
        f.addInput(new Label("Purchase Order ID/#", UIManager.getColor("Label.foreground")), direct);
        JPanel main = new JPanel(new BorderLayout());
        main.add(f, BorderLayout.CENTER);
        JButton find = Elements.button("Find");
        main.add(find, BorderLayout.SOUTH);
        find.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/ORDS/" + direct.getText(), desktop));
                dispose();
            }
        });
        add(main);
    }
}