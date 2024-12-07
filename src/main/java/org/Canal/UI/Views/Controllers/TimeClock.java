package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.DesktopState;

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
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(TimeClock.class.getResource("/icons/timeclock.png")));
        JPanel main = new JPanel(new GridLayout(1, 2));
        JPanel dayControl = new JPanel(new GridLayout(2, 1));
        dayControl.setBorder(new TitledBorder("Hello!"));
        IconButton inForDay = new IconButton("In For Day", "in");
        IconButton outForDay = new IconButton("Out For Day", "out");
        dayControl.add(inForDay);
        dayControl.add(outForDay);
        inForDay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                getEmployeeID("/DAY_IN");
            }
        });
        outForDay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                getEmployeeID("/DAY_OUT");
            }
        });
        JPanel breakControl = new JPanel(new GridLayout(2, 1));
        breakControl.setBorder(new TitledBorder("Break Time"));
        IconButton outForBreak = new IconButton("Out For Break", "lunch");
        IconButton inForBreak = new IconButton("In From Break", "in");
        breakControl.add(outForBreak);
        breakControl.add(inForBreak);
        outForBreak.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                getEmployeeID("/BREAK_IN");
            }
        });
        inForBreak.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                getEmployeeID("/BREAK_OUT");
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

    private void getEmployeeID(String control){
        JInternalFrame frame = new JInternalFrame("Employee ID", false, true, false, false);
        frame.setFrameIcon(new ImageIcon(TimeClock.class.getResource("/icons/employees.png")));
        frame.setLayout(new BorderLayout());
        Form f = new Form();
        f.addInput(new Label("Employee ID", UIManager.getColor("Label.foreground")), new JTextField(10));
        frame.add(f, BorderLayout.CENTER);
        JButton in = new JButton("Continue");
        frame.add(in, BorderLayout.SOUTH);
        desktop.put(frame);
    }
}