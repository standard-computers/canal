package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;

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
        buttons.add(postPosition);

        IconButton assignPosition = new IconButton("Assign", "positions", "Assign position to Employee");
        assignPosition.addActionListener(_ -> {
            String employeeID = JOptionPane.showInputDialog(this, "Enter Employee ID", "Employee ID", JOptionPane.QUESTION_MESSAGE);
            Employee employee = Engine.getEmployee(employeeID);
            if (employee == null) {

            } else {
                employee.setPosition(position.getId());
                position.setStatus(LockeStatus.IN_USE);
//                position.save();
                employee.save();
                dispose();

            }
        });
        buttons.add(assignPosition);

        p.add(buttons, BorderLayout.SOUTH);

        return p;
    }
}