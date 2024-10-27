package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Labels {

    public static JLabel label(String text){
        JLabel l = new JLabel(text);
        l.setBorder(new EmptyBorder(0, 10, 10, 0));
        return l;
    }

    public static JLabel h2(String text){
        JLabel l = new JLabel(text);
        l.setFont(UIManager.getFont("h2.font"));
        l.setBorder(new EmptyBorder(5, 5, 5, 5));
        return l;
    }

    public static JLabel h3(String text){
        JLabel l = new JLabel(text);
        l.setFont(UIManager.getFont("h3.font"));
        l.setBorder(new EmptyBorder(5, 5, 5, 5));
        return l;
    }

    public static JLabel h3(String text, Color color){
        JLabel l = new JLabel(text);
        l.setFont(UIManager.getFont("h3.font"));
        l.setForeground(color);
        l.setBorder(new EmptyBorder(5, 5, 5, 5));
        return l;
    }
}