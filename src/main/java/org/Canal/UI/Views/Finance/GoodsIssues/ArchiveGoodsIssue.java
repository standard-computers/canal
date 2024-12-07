package org.Canal.UI.Views.Finance.GoodsIssues;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /GI/ARCHV
 */
public class ArchiveGoodsIssue extends LockeState {

    public ArchiveGoodsIssue() {
        super("Archive Goods Receipt", "/GI/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}