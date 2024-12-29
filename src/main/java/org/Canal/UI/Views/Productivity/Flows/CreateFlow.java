package org.Canal.UI.Views.Productivity.Flows;

import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * /MVMT/FLWS/NEW
 */
public class CreateFlow extends LockeState {

    private JTextField flowIdField;
    private JTextField flowNameField;

    public CreateFlow() {

        super("Create Flow", "/MVMT/FLWS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateFlow.class.getResource("/icons/create.png")));

        Form f = new Form();
        flowIdField = Elements.input();
        flowNameField = Elements.input();
        f.addInput(new Label("*New Flow ID", UIManager.getColor("Label.foreground")), flowIdField);
        f.addInput(new Label("Flow Name", Constants.colors[0]), flowNameField);


        setLayout(new BorderLayout());
        JPanel info = new JPanel(new BorderLayout());
        info.add(f, BorderLayout.CENTER);
        info.add(options(), BorderLayout.SOUTH);
        add(info, BorderLayout.NORTH);

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setText("{}");
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setCodeFoldingEnabled(true);

        // Set theme (optional)
        try {
            Theme theme = Theme.load(CreateFlow.class.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(textArea);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the RSyntaxTextArea to a JScrollPane
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);

        // Add the scrollPane to the frame
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel options() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from an already created Flow");
        IconButton test = new IconButton("Review", "review", "Review Flow for errors or warnings");
        IconButton save = new IconButton("Save", "save", "Save Flow");
        p.add(copyFrom);
        p.add(test);
        p.add(save);
        return p;
    }
}
