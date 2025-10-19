package org.Canal.UI.Views.Inventory;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.MaterialMovement;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * /STK/MV/FULL
 * For moving the full quantity in an HU to a bin.
 */
public class MoveToBinFull extends LockeState {

    public MoveToBinFull(RefreshListener refreshListener) {

        super("Move Full Stock", "/STK/MV/FULL", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/windows/locke.png")));

        JTextField selectedHU = Elements.input(15);
        JTextField destinationBin = Elements.input();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("HU", Constants.colors[0]), selectedHU);
        form.addInput(Elements.coloredLabel("Destination Bin", Constants.colors[1]), destinationBin);

        setLayout(new BorderLayout());
        add(Elements.header("Move Full Stock", SwingConstants.LEFT), BorderLayout.NORTH);
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
}