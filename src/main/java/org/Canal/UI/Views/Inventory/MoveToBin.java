package org.Canal.UI.Views.Inventory;

import org.Canal.Models.BusinessUnits.Inventory;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /STK/MV/BN
 */
public class MoveToBin extends LockeState {

    public MoveToBin(RefreshListener refreshListener) {

        super("Move Stock", "/STK/MV/BN", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/windows/locke.png")));

        JTextField mvHu = Elements.input(15);
        JTextField destinationBins = Elements.input();
        JTextField mvQty = Elements.input();
        JCheckBox createHu = new JCheckBox("Assigns new HU to SockLine");
        createHu.setSelected(true);

        Form form = new Form();
        form.addInput(Elements.coloredLabel("HU", Constants.colors[0]), mvHu);
        form.addInput(Elements.coloredLabel("Destination Bin", Constants.colors[1]), destinationBins);
        form.addInput(Elements.coloredLabel("Quantity", Constants.colors[2]), mvQty);
        form.addInput(Elements.coloredLabel("Create HU", Constants.colors[3]), createHu);

        setLayout(new BorderLayout());
        add(Elements.header("Move Stock Internally", SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        JButton move = Elements.button("Move Stock");
        add(move, BorderLayout.SOUTH);

        move.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                dispose();

                Inventory i = Engine.getInventory(Engine.getLocation());
                String hu = mvHu.getText();
                boolean success = false;
                double qty = Double.parseDouble(mvQty.getText());

                ArrayList<StockLine> toRemove = new ArrayList<>();
                ArrayList<StockLine> toAdd = new ArrayList<>();
                StockLine modified = null;
                String newHu = Constants.generateId(10);

                for (StockLine s : i.getStockLines()) {
                    if (s.getHu().equals(hu)) {

                        modified = s;
                        double newQty = s.getQuantity() - qty;
                        s.setQuantity(newQty);

                        Item it = Engine.getItem(s.getItem());


                        StockLine nss = new StockLine();
                        nss.setObjex(s.getObjex());
                        nss.setItem(s.getItem());
                        nss.setSku(s.getSku());
                        nss.setQuantity(qty);
                        nss.setValue(qty * it.getPrice());
                        nss.setBin(s.getBin());

                        if (createHu.isSelected()) {
                            newHu = Constants.generateId(10);
                            nss.setHu(newHu);
                        } else {
                            nss.setHu(s.getHu());
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
                if (modified != null) {
                    MaterialMovement mm = new MaterialMovement();
                    mm.setObjex(modified.getObjex());
                    mm.setUser(Engine.getAssignedUser().getId());
                    mm.setSourceBin(modified.getBin());
                    mm.setDestinationBin(destinationBins.getText());
                    mm.setSourceHu(modified.getHu());
                    mm.setDestinationHu(newHu);
                    mm.setQuantity(qty);
                    mm.setStatus(LockeStatus.COMPLETED);
                    i.addMaterialMovement(mm);
                }
                i.save();
                if (success) {
                    refreshListener.refresh();
                }
            }
        });
    }
}