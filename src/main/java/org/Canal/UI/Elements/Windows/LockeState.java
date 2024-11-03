package org.Canal.UI.Elements.Windows;


class LockeState {

    private String lock; //Canal Code
    private boolean duplicated = false; //Can have more than one of these open
    private boolean savable = false; //This Locke state can be saved
    private int[] position = new int[2];

}