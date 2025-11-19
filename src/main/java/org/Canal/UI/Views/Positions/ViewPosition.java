package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;

/**
 * /HR/POS/$[POSITION_ID]
 */
public class ViewPosition extends LockeState {

    private Position position;
    private DesktopState desktop;

    public ViewPosition(Position position, DesktopState desktop) {

        super("View Position", "/HR/POS/" + position.getId());
        this.position = position;
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Information", new JPanel());
        tabs.addTab("Assigned People", new JPanel());

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "Modify Position", "/HR/POS/MOD");
        modify.addActionListener(_ -> {
            dispose();
            desktop.put(new ModifyPosition(position, desktop, null));
        });
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));

//        IconButton create = new IconButton("Create", "create", "Create Position");
//        tb.add(create);
//        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }


}