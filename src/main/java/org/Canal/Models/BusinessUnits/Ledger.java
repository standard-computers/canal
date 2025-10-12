package org.Canal.Models.BusinessUnits;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * LGS
 * Ledger contains a record of certian transactions
 * done through Lockes that should be tracked for
 * business purposes.
 */
public class Ledger extends Objex {

    public String organization;
    public String location = "";
    public String period;
    private String starts;
    private String ends;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Ledger() {
        this.type = "LGS";
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String starts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String ends() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}