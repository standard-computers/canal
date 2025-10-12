package org.Canal.Models;

import org.Canal.Utils.Constants;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.Pipe;

public class Objex {

    protected String type = "";
    protected String id = "";
    protected String name = "";
    protected String creator = "UKNOWN";
    protected String owner = "UKNOWN";
    protected LockeStatus status = LockeStatus.NEW;
    protected String created = Constants.now();
    protected String notes = "";

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void save(){
        Pipe.save(type, this);
    }
}