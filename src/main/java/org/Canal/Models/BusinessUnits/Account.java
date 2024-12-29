package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

public class Account extends Objex {

    private String location;
    private String customer; //Customer ID
    private String opened;
    private String closed;
    private boolean passwordProtected;
    private String password;
    private String agreement; //An Agreement ID, combines locations

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }
}