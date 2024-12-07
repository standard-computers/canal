package org.Canal.UI.Views.Finance.GoodsReceipts;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /GR/ARCHV
 */
public class ArchiveGoodsReceipt extends LockeState {

    public ArchiveGoodsReceipt() {
        super("Archive Goods Receipt", "/GR/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}