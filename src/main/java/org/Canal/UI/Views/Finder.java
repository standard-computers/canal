package org.Canal.UI.Views;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Areas.Areas;
import org.Canal.UI.Views.Items.Items;
import org.Canal.Utils.ConnectDB;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Pipe;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class Finder extends LockeState {

    public Finder(String objex, Object object, DesktopState desktop) {

        super("Find", objex + "/F");
        setFrameIcon(new ImageIcon(Finder.class.getResource("/icons/find.png")));

        Form formPanel = new Form();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();

        int ci = 0;
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String propertyName = entry.getKey();
            if (!propertyName.equals("TYPE") && !propertyName.equals("HPV")) {
                JTextField textField = Elements.input(20);
                formPanel.addInput(Elements.coloredLabel(propertyName, Constants.colors[ci]), textField);
                ci = (ci + 1) % Constants.colors.length;
            }
        }

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(formPanel);
        JScrollPane screr = new JScrollPane(form);
        screr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        screr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        screr.setPreferredSize(new Dimension(500, 375));
        this.add(screr, BorderLayout.CENTER);

        JButton submitButton = Elements.button("Perform Find");
        submitButton.addActionListener(_ -> {
            JsonObject formData = new JsonObject();
            Component[] components = formPanel.getComponents();
            for (int i = 0; i < components.length; i += 2) {
                JLabel label = (JLabel) components[i];
                JTextField textField = (JTextField) components[i + 1];
                formData.addProperty(label.getText(), textField.getText());
            }

            Document filter = new Document();
            for (Map.Entry<String, JsonElement> entry : formData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().getAsString();
                if (!value.isEmpty()) {
                    if (value.startsWith("*") || value.endsWith("*")) {
                        String raw = value.replace("*", "");
                        String regex;

                        if (value.startsWith("*") && value.endsWith("*")) {
                            regex = ".*" + Pattern.quote(raw) + ".*";
                        } else if (value.startsWith("*")) {
                            regex = Pattern.quote(raw) + "$";
                        } else if (value.endsWith("*")) {
                            regex = "^" + Pattern.quote(raw);
                        } else {
                            regex = Pattern.quote(raw);
                        }

                        filter.append(key, new Document("$regex", regex).append("$options", "i"));
                    } else {
                        filter.append(key, value);
                    }
                }
            }

            Class<?> clazz = object.getClass();
            ArrayList<Object> objs = new ArrayList<>();
            MongoCollection<Document> collection = ConnectDB.collection(objex.replaceFirst("/", ""));
            FindIterable<Document> results = collection.find(filter);

            for (Document doc : results) {
                Object instance = Pipe.load(doc, clazz);
                objs.add(instance);
            }

            if (object instanceof Area) {

                desktop.put(new Areas(castList(Area.class, objs), desktop));
            } else if (object instanceof Bin) {

//                desktop.put()
            } else if (object instanceof Item) {

                desktop.put(new Items(castList(Item.class, objs), desktop));
            }
            dispose();
        });

        add(Elements.header("Find by values", SwingConstants.LEFT), BorderLayout.NORTH);
        add(submitButton, BorderLayout.SOUTH);
    }

    public <T> ArrayList<T> castList(Class<? extends T> clazz, ArrayList<?> list) {
        ArrayList<T> result = new ArrayList<>(list.size());
        for (Object o : list) {
            result.add(clazz.cast(o));
        }
        return result;
    }
}