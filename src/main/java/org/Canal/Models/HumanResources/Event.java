package org.Canal.Models.HumanResources;

import org.Canal.Models.Objex;

import java.util.ArrayList;

/**
 * EVTS
 */
public class Event extends Objex {

    private String start;
    private String end;
    private ArrayList<String> attendees; //Objex IDs

    public Event() {
        this.type = "EVTS";
    }

    public Event(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }
}