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

/**
 * /ACCS/NEW
 */
public class CreateAccount extends LockeState {

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
    private JTextField termsQuantityField;

    //Taxes & Rates Tab

    //Notes Tab
    private RTextScrollPane notes;

    public CreateAccount(DesktopState desktop, RefreshListener refreshListener) {

        super("Create an Account", "/ACCS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateAccount.class.getResource("/icons/windows/locke.png")));
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Taxes & Rates", rates());

        if ((boolean) Engine.codex.getValue("ACCS", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Account");
        copyFrom.addActionListener(_ -> {
            String accountId = JOptionPane.showInputDialog("Enter Account ID");
            Account account = Engine.getAccount(accountId);
            if (account == null) {
                accountNameField.setText(account.getName());
                owningLocationField.setText(account.getLocation());
                customerField.setText(account.getCustomer());
                //TODO Open/Close Date(s)
                agreementIdField.setText(account.getAgreement());
                termsQuantityField.setText(String.valueOf(account.getTerms()));
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Details");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Account (Ctrl + S)");
        create.addActionListener(_ -> {

            if (!owningLocationField.getText().isEmpty()) {
                Location l = Engine.getLocationWithId(owningLocationField.getText().trim());
                if (l == null) {
                    JOptionPane.showMessageDialog(null, "Selected owning location does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!customerField.getText().isEmpty()) {
                Location l = Engine.getLocationWithId(customerField.getText().trim());
                if (l == null) {
                    JOptionPane.showMessageDialog(null, "Selected customer does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Account account = new Account();
            String accountId = Engine.generateId("ACCS");
            account.setId(accountId);
            account.setName(accountNameField.getText());
            account.setLocation(owningLocationField.getText());
            account.setCustomer(customerField.getText());
            account.setOpened(openDate.getSelectedDateString());
            account.setClosed(closeDate.getSelectedDateString());
            account.setAgreement(agreementIdField.getText());
            account.setTerms(Double.parseDouble(termsQuantityField.getText()));
            Pipe.save("/ACCS", account);

            dispose();
            if(refreshListener != null) refreshListener.refresh();
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
        termsQuantityField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Account Name", "This is a business', company's, or person's name"), accountNameField);
        form.addInput(Elements.inputLabel("Owning Location", "Location responsible for this account"), owningLocationField);
        form.addInput(Elements.inputLabel("Customer"), customerField);
        form.addInput(Elements.inputLabel("Open Date", "Open/start date of this account"), openDate);
        form.addInput(Elements.inputLabel("Close Date", "When this account will be closed"), closeDate);
        form.addInput(Elements.inputLabel("Attach Agreement ID", "Attach agreement via ID to AutoMake Sales Orders or set other terms"), agreementIdField);
        form.addInput(Elements.inputLabel("Terms (days)", "Number of days from delivery that payment is due"), termsQuantityField);
        general.add(form);

        return general;
    }

    private JPanel rates() {

        JPanel rates = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return rates;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

        if(accountNameField.getText().isEmpty()) {
            addToQueue(new String[]{ "WARNING", "No account name provided." });
        }

        if(owningLocationField.getText().isEmpty()) {
            addToQueue(new String[]{ "WARNING", "Owning location not set. Ledger and billing determination may error." });
        } else {

            //If location selected, ensure it exists
            Location l = Engine.getLocationWithId(owningLocationField.getText().trim());
            if (l == null) {
                addToQueue(new String[]{"CRITICAL", "Owning location selected does not exist!!!"});
            }
        }

        if(customerField.getText().isEmpty()) {
            addToQueue(new String[]{ "CRITICAL", "Account must have customer!!!" });
        } else {

            //If location selected, ensure it exists
            Location l = Engine.getLocationWithId(customerField.getText().trim());
            if (l == null) {
                addToQueue(new String[]{"CRITICAL", "Customer selected does not exist!!!"});
            }
        }

        if(termsQuantityField.getText().isEmpty() || Double.parseDouble(termsQuantityField.getText()) <= 0) {
            addToQueue(new String[]{ "CRITICAL", "Payment terms are set to zero" });
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}