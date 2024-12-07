package org.Canal.UI.Views.Transportation.OutboundDeliveryOrders;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/ODO/ARCHV
 */
public class ArchiveOutboundDeliveryOrder extends LockeState {

    public ArchiveOutboundDeliveryOrder() {
        super("Archive ODO", "/TRANS/ODO/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}