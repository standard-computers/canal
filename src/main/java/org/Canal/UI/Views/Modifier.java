package org.Canal.UI.Views;

import com.google.gson.Gson;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Modifier extends LockeState {

    private String objex;
    private Object newObjex, o;
    private CustomTable objexData;

    public Modifier(String objex, Object newObjex, Object o){
        super("Modifier", objex + "/MOD", false, true, false, true);
        this.objex = objex;
        this.newObjex = newObjex;
        this.o = o;

        setLayout(new BorderLayout());
        add(buttons(), BorderLayout.NORTH);
        add(data(), BorderLayout.CENTER);
    }

    private JPanel buttons(){
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField objexSelector = Elements.input(objex);
            JTextField objexIdField = Elements.input();
        IconButton open = new IconButton("", "open", "");
        IconButton save = new IconButton("", "save", "");

        buttons.add(objexSelector);
        buttons.add(objexIdField);
        buttons.add(open);
        buttons.add(save);
        return buttons;
    }

    private JPanel data() {
        // Create a panel with BorderLayout
        JPanel data = new JPanel(new BorderLayout());

        // Initialize Gson
        Gson gson = new Gson();

        // Convert newObjex to an array of keys/fields
        Object[] templateFields = gson.fromJson(gson.toJson(newObjex), Object[].class);

        // Initialize data list
        ArrayList<Object[]> d = new ArrayList<>();

        // Use reflection to dynamically fetch field values from `o`
        Class<?> clazz = o.getClass();
        for (Object key : templateFields) {
            try {
                // Get the field using its name
                Field field = clazz.getDeclaredField(key.toString());
                field.setAccessible(true); // Make it accessible

                // Add the field name and its value from the `o` object
                d.add(new Object[]{key.toString(), field.get(o)});
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Handle missing fields or access issues
                d.add(new Object[]{key.toString(), "N/A"});
            }
        }

        // Create a custom table with the data
        objexData = new CustomTable(new String[]{"Property", "Value"}, d);

        // Add the table to a scroll pane
        JScrollPane sp = new JScrollPane(objexData);
        data.add(sp, BorderLayout.CENTER);

        return data;
    }
}