package org.Canal.Utils;

import javax.swing.*;

public interface DesktopState {
    void put(JInternalFrame frame);
    void clean();
    void purge();
    void force();
    void setCommander(String text);
}