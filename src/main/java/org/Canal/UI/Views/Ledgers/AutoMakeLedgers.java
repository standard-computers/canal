package org.Canal.UI.Views.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /AREAS/AUTO_MK
 */
public class AutoMakeLedgers extends LockeState {

    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Area Info
    private JTextField ledgerIdField;
    private JTextField ledgerNameField;
    private Selectable periods;
    private DatePicker ledgerStartDate;
    private DatePicker ledgerEndDate;

    //Locations to make Area for
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeLedgers(DesktopState desktop, RefreshListener refreshListener) {

        super("AutoMake Ledgers", "/LGS/AUTO_MK");
        setFrameIcon(new ImageIcon(AutoMakeLedgers.class.getResource("/icons/automake.png")));
        setLayout(new BorderLayout());
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Locations", locationsSelection());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("AutoMake Ledgers", SwingConstants.LEFT), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar(){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton review = new IconButton("Review", "review", "Review Ledger Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("AutoMake", "automake", "Start AutoMake");
        create.addActionListener(_ -> {

            int selected = 0;
            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) selected++;
            }

            Loading loader = new Loading("AutoMaking Ledgers", selected * 4);
            loader.open();
            loader.setVisible(true);

            for (int i = 0; i < checkboxes.size(); i++) {

                JCheckBox checkbox = checkboxes.get(i);
                if (checkbox.isSelected()) {

                    Ledger ledger = new Ledger();
                    ledger.setId(ledgerIdField.getText().trim().replace("@", checkbox.getActionCommand()));
                    ledger.setName(ledgerNameField.getText().replace("@", checkbox.getActionCommand()));
                    ledger.setLocation(locations.get(i).getId());
                    ledger.setPeriod(periods.getSelectedValue());
                    ledger.setStarts(ledgerStartDate.getSelectedDateString());
                    ledger.setEnds(ledgerEndDate.getSelectedDateString());

                    loader.increment();
                    loader.append("Creating Ledger " + ledger.getName() + " (" + ledger.getId() + ")");
                    loader.setMessage("Creating Ledger " + ledger.getName() + " (" + ledger.getId() + ")");

                    Pipe.save("/LGS", ledger);
                }
            }

            loader.close();
            if(refreshListener != null) refreshListener.refresh();
            dispose();
            JOptionPane.showMessageDialog(AutoMakeLedgers.this, "AutoMake Complete");

        });
        tb.add(create);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        panel.add(tb, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        ledgerIdField = Elements.input("2025-@");
        ledgerNameField = Elements.input("2025 @");
        periods = Selectables.periods();
        periods.editable();
        ledgerStartDate = new DatePicker();
        ledgerEndDate = new DatePicker();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*Ledger ID (i.e. 2025-LOCID)"), ledgerIdField);
        form.addInput(Elements.inputLabel("Ledger Name (i.e. 2025 LOCID)"), ledgerNameField);
        form.addInput(Elements.inputLabel("Period"), periods);
        form.addInput(Elements.inputLabel("Start Date"), ledgerStartDate);
        form.addInput(Elements.inputLabel("Close Date"), ledgerEndDate);
        general.add(form);

        return general;
    }

    private JPanel locationsSelection(){

        JPanel locationsSelection = new JPanel(new BorderLayout());
        locations = Engine.getLocations();

        this.checkboxes = new ArrayList<>();
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();

        JScrollPane js = new JScrollPane(checkboxPanel);
        js.setPreferredSize(new Dimension(200, 200));

        JPanel selector = new JPanel(new BorderLayout());

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
                   if(locations.get(i).getType().equals(searchValue.toUpperCase().replace("/", ""))){
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

        selector.add(search, BorderLayout.NORTH);
        selector.add(js, BorderLayout.CENTER);

        JPanel opts = new JPanel(new GridLayout(1, 2));

        JButton selectAll = Elements.button("Select All");
        selectAll.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(true));
                repaint();
            }
        });
        opts.add(selectAll);

        JButton deselectAll = Elements.button("Deselect All");
        deselectAll.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(false));
                repaint();
            }
        });
        opts.add(deselectAll);

        selector.add(opts, BorderLayout.SOUTH);
        locationsSelection.add(selector, BorderLayout.CENTER);

        return locationsSelection;
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

    private void performReview(){


    }
}