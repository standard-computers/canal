package org.Canal.UI.Views.Inventory;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.MaterialMovement;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
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
        HashMap<String, String> putawayBinOptions = new HashMap<>();
        ArrayList<Area> as = (ArrayList<Area>) Engine.getAreas(location);
        for(Area a : as){
            for(Bin b : a.getBins()){
                putawayBinOptions.put(b.getId(), b.getId());
            }
        }
        Selectable destinationBins = new Selectable(putawayBinOptions);
        destinationBins.editable();
        JTextField mvHu = Elements.input();
        JTextField mvQty = Elements.input();
        JCheckBox createHu = new JCheckBox("Assigns new HU to SockLine");
        JCheckBox createWt = new JCheckBox();
        JTextField split = Elements.input();
        createHu.setSelected(true);
        f.addInput(new Label("Destination Bin", Constants.colors[0]), destinationBins);
        f.addInput(new Label("HU", Constants.colors[1]), mvHu);
        f.addInput(new Label("Quantity", Constants.colors[2]), mvQty);
        f.addInput(new Label("Create HU", Constants.colors[3]), createHu);
        f.addInput(new Label("Create WT", Constants.colors[3]), createWt);
        f.addInput(new Label("Split Divisor", Constants.colors[3]), split);
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
                boolean success = false;
                double qty = Double.parseDouble(mvQty.getText());
                int splitDivisor = Integer.parseInt(split.getText());
                qty = qty / splitDivisor;
                for(int a = 0; a < splitDivisor; a++){
                    ArrayList<StockLine> toRemove = new ArrayList<>();
                    ArrayList<StockLine> toAdd = new ArrayList<>();
                    StockLine modified = null;
                    String newHu = Constants.generateId(10);
                    for (StockLine s : i.getStockLines()) {
                        if (s.getHu().equals(hu)) {
                            modified = s;
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
                                newHu = Constants.generateId(10);
                                nss.setHu(newHu);
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
                    if(modified != null){
                        MaterialMovement mm = new MaterialMovement();
                        mm.setObjex(modified.getObjex());
                        mm.setUser(Engine.getAssignedUser().getId());
                        mm.setSourceBin(modified.getBin());
                        mm.setDestinationBin(destinationBins.getSelectedValue());
                        mm.setSourceHu(modified.getHu());
                        mm.setDestinationHu(newHu);
                        mm.setTimestamp(Constants.now());
                        mm.setStatus(LockeStatus.COMPLETED);
                        i.addMaterialMovement(mm);
                    }
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