package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Utils.Constants;

public class MaterialMovement extends Objex {

    private String objex;
    private String user; //User ID of User making move
    private String sourceBin;
    private String destinationBin;
    private String sourceHu;
    private String destinationHu;
    private String timestamp;

    public MaterialMovement(String objex, String user, String sourceBin, String destinationBin, String sourceHu, String destinationHu) {
        this.objex = objex;
        this.user = user;
        this.sourceBin = sourceBin;
        this.destinationBin = destinationBin;
        this.sourceHu = sourceHu;
        this.destinationHu = destinationHu;
        this.timestamp = Constants.now();
    }

    public String getObjex() {
        return objex;
    }

    public void setObjex(String objex) {
        this.objex = objex;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSourceBin() {
        return sourceBin;
    }

    public void setSourceBin(String sourceBin) {
        this.sourceBin = sourceBin;
    }

    public String getDestinationBin() {
        return destinationBin;
    }

    public void setDestinationBin(String destinationBin) {
        this.destinationBin = destinationBin;
    }

    public String getSourceHu() {
        return sourceHu;
    }

    public void setSourceHu(String sourceHu) {
        this.sourceHu = sourceHu;
    }

    public String getDestinationHu() {
        return destinationHu;
    }

    public void setDestinationHu(String destinationHu) {
        this.destinationHu = destinationHu;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}