package org.Canal.UI.Views.Accounts;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ACCS/AUTO_MK
 */
public class AutoMakeAccounts extends LockeState {

    //Operating Objects
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info
    private JTextField accountNameField;
    private JTextField owningLocationField;
    private JTextField customerField;
    private DatePicker openDate;
    private DatePicker closeDate;
    private JTextField agreementIdField;

    //Locations to make Area for
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeAccounts(DesktopState desktop, RefreshListener refreshListener) {

        super("AutoMake Accounts", "/ACCS/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeAccounts.class.getResource("/icons/automake.png")));
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Locations", locations());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Details");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("AutoMake", "automake", "Start AutoMake");
        create.addActionListener(_ -> {

            Account account = new Account();
            String accountId = Engine.generateId("ACCS");
            account.setId(accountId);
            account.setName(accountNameField.getText());
            account.setLocation(owningLocationField.getText());
            account.setCustomer(customerField.getText());
            account.setOpened(openDate.getSelectedDateString());
            account.setClosed(closeDate.getSelectedDateString());
            account.setAgreement(agreementIdField.getText());
            Pipe.save("/ACCS", account);
            dispose();
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-create");
        rp.getActionMap().put("do-create", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

        toolbar.add(Elements.header("Create an Account", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        accountNameField = Elements.input();
        owningLocationField = Elements.input();
        customerField = Elements.input();
        openDate = new DatePicker();
        closeDate = new DatePicker();
        agreementIdField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Account Name", Constants.colors[0]), accountNameField);
        form.addInput(Elements.coloredLabel("Owning Location", Constants.colors[1]), owningLocationField);
        form.addInput(Elements.coloredLabel("Customer", Constants.colors[2]), customerField);
        form.addInput(Elements.coloredLabel("Open Date", Constants.colors[3]), openDate);
        form.addInput(Elements.coloredLabel("Close Date", Constants.colors[4]), closeDate);
        form.addInput(Elements.coloredLabel("Attach Agreement ID", Constants.colors[5]), agreementIdField);
        general.add(form);

        return general;
    }
    private JPanel locations(){

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
                    if(locations.get(i).getType().equals(searchValue.toUpperCase())){
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

    private void performReview() {
    }
}