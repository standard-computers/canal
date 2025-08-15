package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /ORDS/PR/MOD
 */
public class ModifyPurchaseRequisition extends LockeState {

    public ModifyPurchaseRequisition() {
        super("Modify Purchase Req.", "/ORDS/PR/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyPurchaseRequisition.class.getResource("/icons/modify.png")));
    }
}