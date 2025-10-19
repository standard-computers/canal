package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.Models.HumanResources.Timesheet;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TM_CLCK
 */
public class TimeClock extends LockeState {

    private DesktopState desktop;

    public TimeClock(DesktopState desktop) {

        super("Employee Time Clock", "/TM_CLCK", false, true, false, true);
        setFrameIcon(new ImageIcon(TimeClock.class.getResource("/icons/windows/timeclock.png")));
        this.desktop = desktop;

        Employee me = Engine.getEmployee(Engine.getAssignedUser().getEmployee());
        if(me.getPosition() != null){
            Position p = Engine.getPosition(me.getPosition());
            if(!p.isHourly()){
                JOptionPane.showMessageDialog(this, "Your position is salary and does not require clocking in and out.");
            }
        }

        Timesheet ts = Engine.getTimesheet(me.getId());

        JPanel main = new JPanel(new GridLayout(1, 2));
        JPanel dayControl = new JPanel(new GridLayout(2, 1));
        dayControl.setBorder(new TitledBorder("Hello!"));
        IconButton inForDay = new IconButton("In For Day", "in", "");
        IconButton outForDay = new IconButton("Out For Day", "out", "");
        dayControl.add(inForDay);
        dayControl.add(outForDay);
        inForDay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ts.clockIn();
                ts.save();
                JOptionPane.showMessageDialog(TimeClock.this, "In for day has been recorded.");
                dispose();
            }
        });
        outForDay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ts.clockOut();
                ts.save();
                JOptionPane.showMessageDialog(TimeClock.this, "Out for day has been recorded.");
                dispose();
            }
        });
        JPanel breakControl = new JPanel(new GridLayout(2, 1));
        breakControl.setBorder(new TitledBorder("Break Time"));
        IconButton outForBreak = new IconButton("Out For Break", "lunch", "");
        IconButton inForBreak = new IconButton("In From Break", "in", "");
        breakControl.add(outForBreak);
        breakControl.add(inForBreak);
        outForBreak.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        inForBreak.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        main.add(dayControl);
        main.add(breakControl);
        setLayout(new BorderLayout());
        JLabel welcome = Elements.h3("Employee Time Clock");
        JPanel header = new JPanel();
        header.add(welcome);
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }
}