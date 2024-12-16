package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;
import org.Canal.Utils.LockeStatus;

public class MaterialMovement extends Objex {

    private String objex;
    private String user; //User ID of User making move
    private String sourceBin;
    private String destinationBin;
    private String sourceHu;
    private String destinationHu;
    private String timestamp;
    private LockeStatus status = LockeStatus.UNRESTRICTED;
    private String type;

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

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}