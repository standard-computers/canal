package org.Canal.UI.Views.Inventory;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * /STK/MOD/MV
 */
public class MoveStock extends LockeState {

    public MoveStock(String location, RefreshListener refreshListener) {
        super("Move Stock", "/STK/MOD/MV", false, true, false, true);
        Form f = new Form();
        Selectable areas = Selectables.areas();
        HashMap<String, String> putawayBinOptions = new HashMap<>();
        ArrayList<Area> as = (ArrayList<Area>) Engine.getAreas(location);
        for(Area a : as){
            for(Bin b : a.getBins()){
                putawayBinOptions.put(b.getId(), b.getId());
            }
        }
        Selectable destinationBins = new Selectable(putawayBinOptions);
        JTextField mvHu = Elements.input();
        JTextField mvQty = Elements.input();
        JCheckBox createHu = new JCheckBox();
        JCheckBox createWt = new JCheckBox();
        createHu.setSelected(true);
        f.addInput(new Label("Destination Area", UIManager.getColor("Label.foreground")), areas);
        f.addInput(new Label("Destination Bin", Constants.colors[0]), destinationBins);
        f.addInput(new Label("HU", Constants.colors[1]), mvHu);
        f.addInput(new Label("Quantity", Constants.colors[2]), mvQty);
        f.addInput(new Label("Create HU", Constants.colors[3]), createHu);
        f.addInput(new Label("Create WT", Constants.colors[3]), createWt);
        setLayout(new BorderLayout());
        add(Elements.header("Move Stock Internally", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        JButton move = Elements.button("Move Stock");
        add(move, BorderLayout.SOUTH);
        move.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
                JOptionPane.showMessageDialog(MoveStock.this, "Moving Stock");
                Inventory i = Engine.getInventory(location);
                String hu = mvHu.getText();
                double qty = Double.parseDouble(mvQty.getText());
                boolean success = false;

                ArrayList<StockLine> toRemove = new ArrayList<>();
                ArrayList<StockLine> toAdd = new ArrayList<>();
                for (StockLine s : i.getStockLines()) {
                    if (s.getHu().equals(hu)) {
                        double newQty = s.getQuantity() - qty;
                        s.setQuantity(newQty);
                        StockLine nss = new StockLine(
                                s.getObjex(),
                                s.getId(),
                                qty,
                                "",
                                destinationBins.getSelectedValue()
                        );
                        if (createHu.isSelected()) {
                            nss.setHu(Constants.generateId(10));
                        }
                        if (s.getQuantity() <= 0) {
                            toRemove.add(s);
                        }
                        toAdd.add(nss);
                        success = true;
                    }
                }
                for (StockLine rem : toRemove) {
                    i.removeStock(rem);
                }
                for (StockLine add : toAdd) {
                    i.addStock(add);
                }
                i.save();
                JOptionPane.showMessageDialog(MoveStock.this, "Moving Stock Complete!");
                if (success) {
                    refreshListener.onRefresh();
                }
            }
        });
    }
}