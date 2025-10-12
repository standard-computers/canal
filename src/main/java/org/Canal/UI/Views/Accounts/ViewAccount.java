package org.Canal.UI.Views.Accounts;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;

import javax.swing.*;

public class ViewAccount extends LockeState {

    private Account account;
    private DesktopState desktop;

    public ViewAccount(Account account, DesktopState desktop) {

        super(account.getName(), "/ACCS/" + account.getId());
        setFrameIcon(new ImageIcon(ViewAccount.class.getResource("/icons/windows/locke.png")));

        this.account = account;
        this.desktop = desktop;

    }
}