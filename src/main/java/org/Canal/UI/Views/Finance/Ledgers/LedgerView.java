package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;

/**
 * /LGS/$[LEDGER_ID]
 */
public class LedgerView extends JInternalFrame {

    public LedgerView(Ledger l, DesktopState desktop) {
        super("Ledger / " + l.getName() + " – " + l.getId(), true, true, true, true);
        setFrameIcon(new ImageIcon(LedgerView.class.getResource("/icons/ledger.png")));
        setLayout(new BorderLayout());
        add(ledgerOptions(), BorderLayout.NORTH);
    }

    private JPanel ledgerOptions() {
        JPanel panel = new JPanel();

        return panel;
    }
}