package org.Canal.UI.Views.Productivity.Waves;

import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /MVMT/WVS/NEW
 */
public class CreateWave extends LockeState {

    private JTextField waveIdField;
    private JTextField waveDescriptionField;
    private JTextField workOrderIdField;
    private Selectable taskType;

    public CreateWave() {

        super("Create Task", "/MVMT/WVS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateWave.class.getResource("/icons/create.png")));

        Form f = new Form();
        waveIdField = Elements.input();
        waveDescriptionField = Elements.input();
        taskType = Selectables.taskTypes();
        workOrderIdField = Elements.input();
        f.addInput(new Label("*New Task ID", UIManager.getColor("Label.foreground")), waveIdField);
        f.addInput(new Label("Task Type", Constants.colors[0]), taskType);
        f.addInput(new Label("Task Description", Constants.colors[1]), waveDescriptionField);
        f.addInput(new Label("Work Order ID (If Attaching)", Constants.colors[2]), workOrderIdField);

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
