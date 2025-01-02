package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewPosition extends LockeState {

    private Position position;

    public ViewPosition(Position position) {

        super("", "/HR/POS/$", false, true, false, true);
        setFrameIcon(new ImageIcon(ViewPosition.class.getResource("/icons/positions.png")));
        this.position = position;

        setLayout(new BorderLayout());
        add(optionsInfo(), BorderLayout.NORTH);

    }

    private JPanel optionsInfo(){
        JPanel p = new JPanel(new BorderLayout());
        p.add(Elements.header(position.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        IconButton postPosition = new IconButton("Post", "positions", "Post position as available");
        JButton assignPosition = Elements.button("Assign");
        buttons.add(postPosition);
        buttons.add(assignPosition);
        assignPosition.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                // Prompt the user for input
                String userInput = JOptionPane.showInputDialog(null, "Enter employee ID to assign this position to.", "Assign Position", JOptionPane.PLAIN_MESSAGE);

                // Check if input is not null (Cancel returns null)
                if (userInput != null) {
                    // Display the input received
                    Employee em = Engine.getEmployee(userInput);
                    if(em != null) {
                        em.setPosition(position.getId());
                        em.save();
                        JOptionPane.showMessageDialog(null, "Employee Assigned", "Success", JOptionPane.OK_OPTION);
                    }else{
                        JOptionPane.showMessageDialog(null, "Invalid Employee ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Handle cancel case
                    JOptionPane.showMessageDialog(null, "You cancelled the input!", "Cancelled", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }
}
