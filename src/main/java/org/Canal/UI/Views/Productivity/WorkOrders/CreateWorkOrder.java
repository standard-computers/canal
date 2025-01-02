package org.Canal.UI.Views.Productivity.WorkOrders;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;

/**
 * /MVMT/WO/NEW
 */
public class CreateWorkOrder extends LockeState {

    private JTextArea workOrderIdField;

    public CreateWorkOrder(DesktopState desktop) {

        super("Create Work Order", "/MVMT/WO/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateWorkOrder.class.getResource("/icons/create.png")));

        Form f = new Form();


        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Create Task");
        add(create, BorderLayout.SOUTH);


    }
}
