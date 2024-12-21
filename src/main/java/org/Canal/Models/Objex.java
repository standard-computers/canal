package org.Canal.Models;

import org.Canal.Utils.Constants;
import org.Canal.Utils.LockeStatus;

public class Objex {

    protected String id, name, owner = "UKNOWN";
    protected LockeStatus status;
    protected String created = Constants.now();

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
}