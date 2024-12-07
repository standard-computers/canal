package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Distribution.DistributionCenters.DCView;

import javax.swing.*;

/**
 * /CNL/DATA_CNTR
 * This is a holistic object (JSON) editor for master
 * data management.
 */
public class DataCenter extends LockeState {

    public DataCenter() {
        super("Data Center", "/CNL/DATA_CNTR", true, true, true, true);
        setFrameIcon(new ImageIcon(DCView.class.getResource("/icons/datacenter.png")));

    }
}