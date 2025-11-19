package org.Canal.UI.Elements;

import com.formdev.flatlaf.ui.FlatBorder;
import org.Canal.UI.Views.System.Selector;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Selection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SelectionInput extends JPanel implements Selection {

    private JTextField textField;

    public SelectionInput(DesktopState desktop, String objex) {

        Form f = new Form();

        textField = Elements.input(15);
        Border outerBorder = new FlatBorder();
        Border innerPadding = new EmptyBorder(2, 2, 2, 2);
        textField.setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        textField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        JButton selector = new IconButton("", "find", "");
        selector.addActionListener(_ -> desktop.put(new Selector(objex, this)));
        f.addInput(textField, selector);
        add(f);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    @Override
    public void select(String value) {
        textField.setText(value);
    }
}
