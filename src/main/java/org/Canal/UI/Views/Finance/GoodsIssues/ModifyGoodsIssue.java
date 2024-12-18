package org.Canal.UI.Views.Finance.GoodsIssues;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

public class ModifyGoodsIssue extends LockeState {

    public ModifyGoodsIssue() {
        super("Modify GoodsIssue", "/", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyGoodsIssue.class.getResource("/icons/modify.png")));
    }
}