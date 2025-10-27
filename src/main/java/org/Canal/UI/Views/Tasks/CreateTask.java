package org.Canal.UI.Views.Tasks;

import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * /MVMT/TSKS/NEW
 */
public class CreateTask extends LockeState {

    //Operating Objects
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info Tab
    private JTextField taskDescription;
    private JTextField workOrderId;
    private JTextField taskCompleter;
    private JTextField locations;
    private JTextField employees;

    //Controls Tab
    private Selectable taskStatus;
    private JCheckBox createWorkOrder;
    private JTextField sourceArea;
    private JTextField sourceBin;
    private JTextField destintationArea;
    private JTextField destintationBin;
    private JTextField quantity;
    private JCheckBox doGoodsReceipt;
    private JCheckBox doGoodsIssue;


    public CreateTask(DesktopState desktop, RefreshListener refreshListener) {

        super("Create Task", "/MVMT/TSKS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateTask.class.getResource("/icons/create.png")));
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());

        setLayout(new BorderLayout());

        add(Elements.header("Create a Task", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        JButton create = Elements.button("Create Task");
        create.addActionListener(_ -> {

            Task task = new Task();
            task.setId(Engine.generateId("MVMT/TSKS"));
            task.setDescription(taskDescription.getText().trim());
            task.setLocation(locations.getText().trim());
            task.setSourceArea(sourceArea.getText().trim());
            task.setSourceBin(sourceBin.getText().trim());
            task.setDestinationArea(destintationArea.getText().trim());
            task.setDestinationBin(destintationBin.getText().trim());
            task.setEmployee(employees.getText().trim());

            if (createWorkOrder.isSelected()) {
                //TODO Create Work Order
            } else {
                if (!workOrderId.getText().isEmpty()) {
                    task.setWorkOrder(workOrderId.getText().trim());
                }
            }

            Pipe.save("/MVMT/TSKS", task);
            dispose();

        });
        add(create, BorderLayout.SOUTH);
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taskDescription = Elements.input();
        workOrderId = Elements.input();
        locations = Elements.input();
        employees = Elements.input();
        taskCompleter = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Task Description"), taskDescription);
        form.addInput(Elements.inputLabel("Work Order ID (If Attaching)"), workOrderId);
        form.addInput(Elements.inputLabel("Assigned Location"), locations);
        form.addInput(Elements.inputLabel("Assigned Employee"), employees);
        form.addInput(Elements.inputLabel("Locke to Complete"), taskCompleter);
        general.add(form);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taskStatus = Selectables.taskTypes();
        createWorkOrder = new JCheckBox("Create Work Order");
        sourceArea = Elements.input();
        sourceBin = Elements.input();
        destintationArea = Elements.input();
        destintationBin = Elements.input();
        quantity = Elements.input();
        doGoodsReceipt = new JCheckBox("Creates GR on Confirmation for Destination");
        doGoodsIssue = new JCheckBox("Creates GI on Confirmation for Source ");

        Form form = new Form();
        form.addInput(Elements.inputLabel("Create with Status"), taskStatus);
        form.addInput(Elements.inputLabel("Create Work Order"), createWorkOrder);
        controls.add(form);

        return controls;
    }
}