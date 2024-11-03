package org.Canal.Models.BusinessUnits;

import org.Canal.Utils.Locke;

public class LedgerCommit {

    private String id; //
    private String user; //User ID of committing user
    private Locke locke; //Canal transaction was commited with
    private String objex; //Locke Code for Objex type
    private String committed; //Timestamp transaction committed

}