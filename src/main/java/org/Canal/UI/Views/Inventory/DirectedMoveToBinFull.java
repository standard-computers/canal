package org.Canal.UI.Views.Inventory;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;

/**
 * /STK/MV/DFULL
 * For moving the full quantity in an HU to a bin with predetermined destination.
 */
public class DirectedMoveToBinFull extends LockeState {

    private JTextField selectedHU = Elements.input(15);

    public DirectedMoveToBinFull(RefreshListener refreshListener) {

        super("Directed Move Full Stock", "/STK/MV/DFULL", false, true, false, true);

        JTextField destinationBin = Elements.input();
        for (Area a : Engine.getAreas(Engine.getLocation())) {
            if (a.allowsInventory()) {
                for (Bin b : Engine.getBinsForArea(a.getId())) {
                    if (b.putawayEnabled() && !b.getStatus().equals(LockeStatus.BLOCKED) && !b.hasInventory()) {
                        destinationBin.setText(b.getId());
                        break;
                    }
                }
            }
        }

        Form form = new Form();
        form.addInput(Elements.inputLabel("HU (Handling Unit)"), selectedHU);
        form.addInput(Elements.inputLabel("Destination Bin"), destinationBin);

        setLayout(new BorderLayout());
        add(Elements.header("Directed Move HU to Bin", SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        JButton move = Elements.button("Move");
        add(move, BorderLayout.SOUTH);

        move.addActionListener(_ -> {

            Bin bin = Engine.getBin(destinationBin.getText().trim());
            if (bin == null) {

                JOptionPane.showMessageDialog(null, "Destination bin not found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                if (!bin.putawayEnabled()) {

                    JOptionPane.showMessageDialog(null, "Bin does not allow put away", "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    dispose();

                    Inventory i = Engine.getInventory(Engine.getLocation());
                    String hu = selectedHU.getText();

                    for (StockLine s : i.getStockLines()) {
                        if (s.getHu().equals(hu)) {

                            Item it = Engine.getItem(s.getItem());
                            StockLine newStockLine = new StockLine();
                            newStockLine.setId(Constants.generateId(6));
                            newStockLine.setObjex(s.getObjex());
                            newStockLine.setItem(s.getItem());
                            newStockLine.setSku(s.getSku());
                            newStockLine.setQuantity(s.getQuantity());
                            newStockLine.setValue(s.getQuantity() * it.getPrice());
                            newStockLine.setBin(destinationBin.getText().trim());
                            newStockLine.setHu(s.getHu());
                            newStockLine.setReceipt(Constants.now());
                            i.removeStock(s);
                            i.addStock(newStockLine);

                            MaterialMovement mm = new MaterialMovement();
                            mm.setObjex(s.getObjex());
                            mm.setUser(Engine.getAssignedUser().getId());
                            mm.setSourceBin(s.getBin());
                            mm.setDestinationBin(destinationBin.getText().trim());
                            mm.setSourceHu(s.getHu());
                            mm.setDestinationHu(s.getHu());
                            mm.setQuantity(s.getQuantity());
                            mm.setStatus(LockeStatus.COMPLETED);
                            i.addMaterialMovement(mm);

                            i.save();
                            if (refreshListener != null) refreshListener.refresh();
                            return;
                        }
                    }
                }
            }
        });
    }

    public void setHu(String hu) {
        selectedHU.setText(hu);
        repaint();
        revalidate();
    }
}