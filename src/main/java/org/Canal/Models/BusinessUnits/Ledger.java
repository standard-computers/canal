package org.Canal.Models.BusinessUnits;

import org.Canal.Models.SupplyChainUnits.Transaction;
import org.Canal.Utils.Engine;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Ledger {

    private String id, name, org, period;
    private long starts, ends;
    private int created, closed;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Ledger(String id, String name) {
        this.id = id;
        this.name = name;
        this.org = Engine.getOrganization().getId();
        this.period = id;
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime startOfYear = LocalDateTime.of(currentYear, 1, 1, 0, 0);
        starts = startOfYear.toEpochSecond(ZoneOffset.UTC);
        LocalDateTime endOfYear = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59);
        ends = endOfYear.toEpochSecond(ZoneOffset.UTC);
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

    public String getOrg() {
        return org;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getStarts() {
        return starts;
    }

    public long getEnds() {
        return ends;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}