package org.Canal.UI.Views.Finance.Accounts;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /ACCS/NEW
 */
public class CreateAccount extends LockeState {

    public CreateAccount() {

        super("Create an Account", "/ACCS/NEW", false, true, false, true);

        JTextField accountIdField = Elements.input();
        Selectable locations = Selectables.allLocations();
        Selectable customers = Selectables.customers();
        DatePicker openDate = new DatePicker();
        DatePicker closeDate = new DatePicker();
        JTextField agreementField = Elements.input();
        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New Account ID (Account #/Payer #)", UIManager.getColor("Label.foreground")), accountIdField);
        f.addInput(Elements.coloredLabel("Owning Location", Constants.colors[10]), locations);
        f.addInput(Elements.coloredLabel("Customer", Constants.colors[9]), customers);
        f.addInput(Elements.coloredLabel("Open Date", Constants.colors[8]), openDate);
        f.addInput(Elements.coloredLabel("Close Date", Constants.colors[7]), closeDate);
        f.addInput(Elements.coloredLabel("Attach Agreement ID", Constants.colors[7]), agreementField);

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Create");
        add(create, BorderLayout.SOUTH);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                Account newAccount = new Account();
                newAccount.setId(accountIdField.getText());
                newAccount.setCustomer(customers.getSelectedValue());
                newAccount.setOpened(openDate.getSelectedDateString());
                newAccount.setClosed(closeDate.getSelectedDateString());
                newAccount.setAgreement(agreementField.getText());
                Pipe.save("/ACCS", newAccount);
                dispose();
                JOptionPane.showMessageDialog(null, "Account Created");
            }
        });
    }
}