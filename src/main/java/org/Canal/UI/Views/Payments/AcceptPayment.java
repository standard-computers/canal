package org.Canal.UI.Views.Payments;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

public class AcceptPayment extends LockeState {

    public AcceptPayment() {
        super("Process Payment", "/", false, true, false, true);
        JButton process = new JButton("Process");
        setLayout(new BorderLayout());
        add(process, BorderLayout.SOUTH);
    }
}