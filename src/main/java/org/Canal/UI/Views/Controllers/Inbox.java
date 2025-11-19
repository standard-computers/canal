package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;

/**
 * /INBOX
 */
public class Inbox extends LockeState {

    public Inbox(DesktopState desktop) {

        super("Inbox", "/INBOX");

        setMaximized(true);
    }
}
