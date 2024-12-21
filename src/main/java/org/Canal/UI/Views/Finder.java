package org.Canal.UI.Views;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Finder extends LockeState {

    public Finder(String objex, DesktopState desktop){
        super("Find", objex + "/F", false, true, false, true);
        setFrameIcon(new ImageIcon(Finder.class.getResource("/icons/find.png")));

        // Set the layout for the Finder
        Form formPanel = new Form();
        Gson gson = new Gson();
        JsonObject jsonObject = Pipe.get(objex);

        int ci = 0;
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String propertyName = entry.getKey();
            if(!propertyName.equals("TYPE") && !propertyName.equals("HPV")) {
                JTextField textField = Elements.input(20);
                formPanel.addInput(new Label(propertyName, Constants.colors[ci]), textField);
                ci = (ci + 1) % Constants.colors.length;
            }
        }

        // Add the form panel to the Finder
        JScrollPane screr = new JScrollPane(formPanel);
        screr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        screr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        screr.setPreferredSize(new Dimension(400, 300));
        this.add(screr, BorderLayout.CENTER);

        // Add a submit button at the bottom
        JButton submitButton = Elements.button("Perform Find");
        submitButton.addActionListener(e -> {
            // Collect the data from the form and handle submission
            JsonObject formData = new JsonObject();
            Component[] components = formPanel.getComponents();
            for (int i = 0; i < components.length; i += 2) {
                JLabel label = (JLabel) components[i];
                JTextField textField = (JTextField) components[i + 1];
                formData.addProperty(label.getText(), textField.getText());
            }

            System.out.println("Form Data: " + gson.toJson(formData));
        });
        add(Elements.header("Find by values", SwingConstants.LEFT), BorderLayout.NORTH);
        add(submitButton, BorderLayout.SOUTH);
    }
}