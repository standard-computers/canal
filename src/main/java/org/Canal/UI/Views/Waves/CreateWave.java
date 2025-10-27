package org.Canal.UI.Views.Waves;

import org.Canal.UI.Elements.*;

import javax.swing.*;
import java.awt.*;

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

        waveIdField = Elements.input();
        waveDescriptionField = Elements.input();
        taskType = Selectables.taskTypes();
        workOrderIdField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Task ID"), waveIdField);
        form.addInput(Elements.inputLabel("Task Type"), taskType);
        form.addInput(Elements.inputLabel("Task Description"), waveDescriptionField);
        form.addInput(Elements.inputLabel("Work Order ID (If Attaching)"), workOrderIdField);

        setLayout(new BorderLayout());
        add(Elements.header("Create a Task", SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        JButton create = Elements.button("Create Task");
        add(create, BorderLayout.SOUTH);
        create.addActionListener(_ -> {

        });
    }
}
