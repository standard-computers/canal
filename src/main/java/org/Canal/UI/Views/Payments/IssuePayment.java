package org.Canal.UI.Views.Payments;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /FIN/PYMNTS/NEW
 */
public class IssuePayment extends LockeState {

    public IssuePayment() {
        super("Issue Payment", "/FIN/PYMNTS/NEW", false, true, false, true);
        JButton process = new JButton("Process");
        setLayout(new BorderLayout());
        add(process, BorderLayout.SOUTH);
    }
}