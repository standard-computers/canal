package org.Canal.UI.Views.Productivity.Tasks;

import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /MVMT/TSKS/NEW
 */
public class CreateTask extends LockeState {

    private JTextField taskIdField;
    private JTextField taskDescriptionField;
    private JTextField workOrderIdField;
    private JTextField taskCompleter;
    private JTextField objexId;
    private Selectable taskType;
    private Selectable locations;
    private Selectable employees;

    public CreateTask() {

        super("Create Task", "/MVMT/TSKS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateTask.class.getResource("/icons/create.png")));

        Form f = new Form();
        taskIdField = Elements.input();
        taskDescriptionField = Elements.input();
        taskType = Selectables.taskTypes();
        workOrderIdField = Elements.input();
        locations = Selectables.allLocations();
        employees = Selectables.employees();
        taskCompleter = Elements.input();
        objexId = Elements.input();

        f.addInput(Elements.coloredLabel("*New Task ID", UIManager.getColor("Label.foreground")), taskIdField);
        f.addInput(Elements.coloredLabel("Task Type", Constants.colors[0]), taskType);
        f.addInput(Elements.coloredLabel("Task Description", Constants.colors[1]), taskDescriptionField);
        f.addInput(Elements.coloredLabel("Work Order ID (If Attaching)", Constants.colors[2]), workOrderIdField);
        f.addInput(Elements.coloredLabel("Assigned Location", Constants.colors[3]), locations);
        f.addInput(Elements.coloredLabel("Assigned Employee", Constants.colors[4]), employees);
        f.addInput(Elements.coloredLabel("Locke to Complete", Constants.colors[5]), taskCompleter);
        f.addInput(Elements.coloredLabel("For Objex ID", Constants.colors[6]), objexId);

        setLayout(new BorderLayout());
        add(Elements.header("Create a Task", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Create Task");
        add(create, BorderLayout.SOUTH);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

            }
        });
    }
}