package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {

    public Label(String text, Color color){
        super(text);
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.BOLD, 12));
        setMinimumSize(new Dimension(120, 25));
        setMaximumSize(new Dimension(200, 25));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 1, 0, color),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
}