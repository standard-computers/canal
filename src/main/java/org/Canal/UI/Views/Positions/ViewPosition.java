package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /HR/POS/$[POSITION_ID]
 */
public class ViewPosition extends LockeState {

    private Position position;

    public ViewPosition(Position position) {

        super("", "/HR/POS/$", false, true, false, true);
        setFrameIcon(new ImageIcon(ViewPosition.class.getResource("/icons/positions.png")));
        this.position = position;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Information", new JPanel());
        tabs.addTab("Assigned People", new JPanel());

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar(){

        JPanel p = new JPanel(new BorderLayout());
        p.add(Elements.header(position.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        IconButton postPosition = new IconButton("Post", "positions", "Post position as available");
        IconButton assignPosition = new IconButton("Assign", "positions", "Assign position to Employee");
        buttons.add(postPosition);
        buttons.add(assignPosition);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }
}