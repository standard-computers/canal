package org.Canal.UI.Views.System;

import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Productivity.Flows.CreateFlow;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.util.ArrayList;

/**
 * /$[LOCKE]/>
 */
public class LockeMessages extends LockeState {

    public LockeMessages(ArrayList<String[]> messages) {

        super("Locke Messages", "/>$");
        setFrameIcon(new ImageIcon(LockeMessages.class.getResource("/icons/alerts.png")));

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        for(String[] m : messages) {
            textArea.append(m[0] + " / " + m[1] + "\n");
        }
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setCodeFoldingEnabled(true);
        try {
            Theme theme = Theme.load(CreateFlow.class.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(textArea);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        add(scrollPane);
    }
}