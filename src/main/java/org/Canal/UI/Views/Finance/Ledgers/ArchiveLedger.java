package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /LGS/ARCHV
 */
public class ArchiveLedger extends LockeState {

    public ArchiveLedger() {
        super("Archive Ledger", "/LGS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}