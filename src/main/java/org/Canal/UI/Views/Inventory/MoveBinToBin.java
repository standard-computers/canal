package org.Canal.UI.Views.Inventory;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.MaterialMovement;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * /STK/MV/BB
 */
public class MoveBinToBin extends LockeState {

    public MoveBinToBin(RefreshListener refreshListener) {

        super("Move Bin to Bin", "/STK/MV/BB", false, true, false, true);

        JTextField sourceBin = Elements.input(15);
        JTextField destinationBin = Elements.input();
        JCheckBox createHu = new JCheckBox("Assigns new HU to SockLine");
        createHu.setSelected(true);

        Form form = new Form();
        form.addInput(Elements.inputLabel("Source Bin"), sourceBin);
        form.addInput(Elements.inputLabel("Destination Bin"), destinationBin);

        setLayout(new BorderLayout());
        add(Elements.header("Move Stock", SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        JButton move = Elements.button("Move");
        add(move, BorderLayout.SOUTH);

        move.addActionListener(_ -> {

            Bin sBin = Engine.getBin(sourceBin.getText().trim());
            Bin dBin = Engine.getBin(destinationBin.getText().trim());
            if (dBin == null) {
                JOptionPane.showMessageDialog(null, "Destination bin not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!dBin.putawayEnabled() && !dBin.getStatus().equals(LockeStatus.BLOCKED)) {
                JOptionPane.showMessageDialog(null, "Destination bin does not allow put away", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sBin == null) {
                JOptionPane.showMessageDialog(null, "Source bin not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!sBin.pickingEnabled() && !sBin.getStatus().equals(LockeStatus.BLOCKED)) {
                JOptionPane.showMessageDialog(null, "Source bin does not allow picking", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();

            Inventory i = Engine.getInventory(Engine.getLocation());
            String hu = sourceBin.getText();

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
                    JOptionPane.showMessageDialog(MoveBinToBin.this, "Moving Stock Complete!");
                    if (refreshListener != null) refreshListener.refresh();
                    return;
                }
            }
        });
    }
}