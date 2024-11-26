package org.Canal.Models.BusinessUnits;

import org.Canal.Models.SupplyChainUnits.Transaction;
import org.Canal.Start;
import org.Canal.Utils.Json;
import org.Canal.Utils.LockeStatus;

import java.io.File;
import java.util.ArrayList;

/**
 * Ledger contains a record of certian transactions
 * done through Lockes that should be tracked for
 * business purposes.
 */
public class Ledger {

    private String id;
    public String name;
    public String org;
    public String period;
    private String starts;
    private String ends;
    private String created;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private LockeStatus status = LockeStatus.NEW;

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

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public LockeStatus getStatus() {
        return status;
    }

    public void setStatus(LockeStatus status) {
        this.status = status;
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

    public void save(){
        File md = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\LGS\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".lgs")) {
                    Ledger fl = Json.load(file.getPath(), Ledger.class);
                    if (fl.getId().equals(id)) {
                        Json.save(file.getPath(), this);
                        break;
                    }
                }
            }
        }
    }
}