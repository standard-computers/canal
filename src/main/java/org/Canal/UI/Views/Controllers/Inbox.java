package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;

import javax.swing.*;

/**
 * /CNL/INB, /INBOX
 */
public class Inbox extends LockeState {

    public Inbox(DesktopState desktop) {

        super("Inbox", "/INBOX", false, true, false, true);
        setFrameIcon(new ImageIcon(Inbox.class.getResource("/icons/inbox.png")));


    }
}
