package org.Canal.UI.Views.Accounts;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

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
    private DatePicker openDate;
    private DatePicker closeDate;
    private JTextField agreementIdField;
    private JTextField termsQuantityField;

    //Locations to make Area for
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;

    //Notes Tab
    private RTextScrollPane notes;

    public AutoMakeAccounts(DesktopState desktop, RefreshListener refreshListener) {

        super("AutoMake Accounts", "/ACCS/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeAccounts.class.getResource("/icons/automake.png")));
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Locations", locations());

        if ((boolean) Engine.codex.getValue("ACCS", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

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
            account.setOpened(openDate.getSelectedDateString());
            account.setClosed(closeDate.getSelectedDateString());
            account.setAgreement(agreementIdField.getText());
            Pipe.save("/ACCS", account);

            dispose();
            if (refreshListener != null) refreshListener.refresh();
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
        openDate = new DatePicker();
        closeDate = new DatePicker();
        agreementIdField = Elements.input();
        termsQuantityField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Account Name", "This is a business', company's, or person's name"), accountNameField);
        form.addInput(Elements.inputLabel("Owning Location", "Location responsible for this account"), owningLocationField);
        form.addInput(Elements.inputLabel("Open Date", "Open/start date of this account"), openDate);
        form.addInput(Elements.inputLabel("Close Date", "When this account will be closed"), closeDate);
        form.addInput(Elements.inputLabel("Attach Agreement ID", "Attach agreement via ID to AutoMake Sales Orders or set other terms"), agreementIdField);
        form.addInput(Elements.inputLabel("Terms (In DAYS)", "Number of days from delivery that payment is due"), termsQuantityField);
        general.add(form);

        return general;
    }

    private JPanel locations() {

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
            if (searchValue.endsWith("*")) { //Searching for ID starts with
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            } else if (searchValue.startsWith("/")) { //Objex type selection

                for (int i = 0; i < checkboxes.size(); i++) {
                    if (locations.get(i).getType().equals(searchValue.toUpperCase())) {
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

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

        if (accountNameField.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "No account name provided."});
        }

        if (owningLocationField.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Owning location not set. Ledger and billing determination may error."});
        }

        if (termsQuantityField.getText().isEmpty() || Double.parseDouble(termsQuantityField.getText()) <= 0) {
            addToQueue(new String[]{"CRITICAL", "Payment terms are set to zero"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}