package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import javax.swing.*;
import java.awt.*;

/**
 * /ORDS/PR/MOD
 */
public class ModifyPurchaseRequisition extends JInternalFrame {

    public ModifyPurchaseRequisition() {
        super("Modify Purchase Req.", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyPurchaseRequisition.class.getResource("/icons/modify.png")));
    }
}