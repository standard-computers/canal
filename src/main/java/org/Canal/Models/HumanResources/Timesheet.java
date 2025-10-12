package org.Canal.Models.HumanResources;

import org.Canal.Models.Objex;
import org.Canal.Utils.Constants;

import java.util.ArrayList;

/**
 * HR/TMSH
 */
public class Timesheet extends Objex {

    private String employee;
    private ArrayList<Event> events = new ArrayList<>();

    public Timesheet() {
        this.type = "HR/TMSH";
    }

    public Timesheet(String employee) {
        this.employee = employee;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void addEntry(Event event) {
        events.add(event);
    }

    public void clockIn() {
        events.add(new Event(Constants.now()));
    }

    public void clockOut() {
        for (Event event : events.reversed()) {
            if (event.getEnd() == null) {
                event.setEnd(Constants.now());
                return;
            }
        }
    }
}