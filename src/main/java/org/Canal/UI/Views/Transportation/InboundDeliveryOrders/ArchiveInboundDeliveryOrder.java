package org.Canal.UI.Views.Transportation.InboundDeliveryOrders;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/IDO/ARCHV
 */
public class ArchiveInboundDeliveryOrder extends LockeState {

    public ArchiveInboundDeliveryOrder() {
        super("Archive IDO", "/TRANS/IDO/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}