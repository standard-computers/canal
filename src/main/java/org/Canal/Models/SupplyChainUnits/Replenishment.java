package org.Canal.Models.SupplyChainUnits;

import org.Canal.Models.Objex;

public class Replenishment extends Objex {

    private int cadence; //How often in seconds to run this
    private int delay = 0; //Delay in seconds from cadence start
    private boolean autoTask; //Auto create tasks required for this replenishment
    private boolean autoWave; //Auto create waves required for this replenishment

}