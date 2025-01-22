package org.Canal.UI.Views.Products.Materials;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;

/**
 * /MTS/$[MATERIAL_ID]
 */
public class ViewMaterial extends LockeState {

    private Item material;

    public ViewMaterial(Item material) {

        super("Material / " + material.getName(), "/MTS/$", true, true, true, true);
        setFrameIcon(new ImageIcon(ViewMaterial.class.getResource("/icons/materials.png")));
        this.material = material;



    }
}