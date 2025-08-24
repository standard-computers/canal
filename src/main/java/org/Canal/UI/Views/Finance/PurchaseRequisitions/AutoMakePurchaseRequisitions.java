package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * /ORDS/PR/AUTO_MK
 */
public class AutoMakePurchaseRequisitions extends LockeState {

    private DesktopState desktop;
    private JPanel checkboxPanel;
    private JTextField descriptionField;
    private JTextField maxSpendField;
    private JCheckBox isSingleOrder;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes = new ArrayList<>();
    private DatePicker prStartDateField;
    private DatePicker prEndDateField;
    private JTextField suppliers;
    private RTextScrollPane notes;

    public AutoMakePurchaseRequisitions(DesktopState desktop) {

        super("AutoMake Purchase Reqs.", "/ORDS/PR/AUTO_MK");
        setFrameIcon(new ImageIcon(AutoMakePurchaseRequisitions.class.getResource("/icons/automake.png")));
        this.desktop = desktop;
        locations = Engine.getLocations();

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Information", information());
        tabs.addTab("Items", items());
        tabs.addTab("Buyers", buyers());
        tabs.addTab("Notes", notes());


        JPanel p = new JPanel(new BorderLayout());
        p.add(Elements.header("AutoMake Purchase Requisitions", SwingConstants.LEFT), BorderLayout.NORTH);
        p.add(toolbar(), BorderLayout.SOUTH);

        add(p, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copy = new IconButton("Copy From", "open", "Copy from PR");
        tb.add(copy);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "Review", "Review AutoMake");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton automake = new IconButton("Start AutoMake", "automake", "AutoMake");
        automake.addActionListener(_ -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for(int i = 0; i < checkboxes.size(); i++) {
                JCheckBox checkbox = checkboxes.get(i);

                if (checkbox.isSelected()) {
                    String genId = "PR" + (10000000 + (Engine.getPurchaseRequisitions().size() + 1));
                    String genIn = String.valueOf((10000000 + (Engine.getPurchaseRequisitions().size() + 1)));
                    PurchaseRequisition preq = new PurchaseRequisition();
                    preq.setId(genId);
                    preq.setNumber(genIn);
                    preq.setName(genId);
                    preq.setOwner("ORDS/PR/AUTO_MK");
                    preq.setSupplier(suppliers.getText());
                    preq.setBuyer(locations.get(i).getId());
                    preq.setMaxSpend(Double.valueOf(maxSpendField.getText()));
                    preq.setStart(dateFormat.format(prStartDateField.getSelectedDate()));
                    preq.setEnd(dateFormat.format(prEndDateField.getSelectedDate()));
                    preq.setNotes(notes.getTextArea().getText());
                    Pipe.save("/ORDS/PR", preq);
                }
            }
            dispose();
            JOptionPane.showMessageDialog(null, "AutoMake Purchase Reqs Complete");
        });
        tb.add(automake);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        return tb;
    }

    private JPanel information() {

        JPanel information = new JPanel(new FlowLayout(FlowLayout.LEFT));

        descriptionField = Elements.input(10);
        maxSpendField = Elements.input();
        isSingleOrder = new JCheckBox();
        suppliers = Elements.input();
        prStartDateField = new DatePicker();
        prEndDateField = new DatePicker();

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Description", Constants.colors[9]), descriptionField);
        f.addInput(Elements.coloredLabel("Max Spend", Constants.colors[8]), maxSpendField);
        f.addInput(Elements.coloredLabel("[or] Single Order", UIManager.getColor("Label.foreground")), isSingleOrder);
        f.addInput(Elements.coloredLabel("Supplier", Constants.colors[7]), suppliers);
        f.addInput(Elements.coloredLabel("Start Date", Constants.colors[6]), prStartDateField);
        f.addInput(Elements.coloredLabel("End Date", Constants.colors[5]), prEndDateField);
        information.add(f);

        return information;
    }

    private JPanel items(){
        JPanel items = new JPanel(new BorderLayout());

        return items;
    }

    private JPanel buyers(){

        JPanel buyers = new JPanel(new BorderLayout());

        JTextField search = Elements.input();
        search.addActionListener(_ -> {
           String searchValue = search.getText().trim();
            if(searchValue.endsWith("*")){ //Searching for ID starts with
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            } else if (searchValue.startsWith("/")) { //Objex type selection

                for (int i = 0; i < checkboxes.size(); i++) {
                    if(locations.get(i).getType().equals(searchValue)){
                        checkboxes.get(i).setSelected(!checkboxes.get(i).isSelected());
                    }
                }

            } else { //Select exact match
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().equals(searchValue)) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            }
        });

        buyers.add(search, BorderLayout.NORTH);

        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();
        JScrollPane sp = new JScrollPane(checkboxPanel);
        sp.setPreferredSize(new Dimension(300, 300));

        buyers.add(sp, BorderLayout.CENTER);

        return buyers;
    }

    private void addCheckboxes() {

        for (Location location : locations) {
            String displayText = location.getId() + " - " + location.getName();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(location.getId())); // Set the value as ID
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }

    private JScrollPane notes(){

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview(){

        //Warn if no PR description
        if(descriptionField.getText().isEmpty()){
            addToQueue(new String[]{"WARNING", "No Purchase Req description set. Are you sure?"});
        }

        //Check if supplier selected and validity
        if(suppliers.getText().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "NO SUPPLIER SELECTED!!!"});
        } else {
            Location foundVendor = Engine.getLocation(suppliers.getText(), "VEND");
            if(foundVendor == null){
                addToQueue(new String[]{"CRITICAL", "SELECTED SUPPLIER DOES NOT EXIST!!!"});
            }
        }

        //Check buyer count, must be greater than 0
        int buyerCount = 0;
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                buyerCount++;
            }
        }
        if(buyerCount == 0){
            addToQueue(new String[]{"CRITICAL", "NO BUYERS SELECTED!!!"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}
