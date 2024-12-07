package org.Canal.UI.Views.HR.Organizations;

import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;

/**
 * /ORGS/ARCHV
 */
public class ArchiveOrganizations extends LockeState {

    public ArchiveOrganizations() {
        super("Archive Organization", "/ORGS/ARCHV", false, true, false, true);
        if(Engine.getOrganizations().size() > 1){

            JOptionPane.showConfirmDialog(this, "WARNING", "Archiving this organization would remove all data from Canal associated with this organization that may be used elsewhere. Are you sure you want to do this?", 1);
        }else{
            JOptionPane.showConfirmDialog(this, "WARNING", "There is only one organization. Archiving this organization would effectively remove all data from Canal. Are you sure you want to do this?", 1);
        }
    }
}