package org.Canal.Models.HumanResources;

import org.Canal.Models.Objex;
import org.Canal.Start;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Json;

import java.io.File;
import java.util.ArrayList;

/**
 * /HR/TMSH
 */
public class Timesheet extends Objex {

    private String employee;
    private ArrayList<Event> events = new ArrayList<>();

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

    public void clockIn(){
        events.add(new Event(Constants.now()));
    }

    public void clockOut(){
        for (Event event : events.reversed()) {
            if(event.getEnd() == null){
                event.setEnd(Constants.now());
                return;
            }
        }
    }

    public void save(){
        File md = new File(Start.DIR + "\\.store\\HR\\TMSH\\");
        File[] mdf = md.listFiles();
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".hr.tmsh")) {
                    Timesheet fl = Json.load(file.getPath(), Timesheet.class);
                    if (fl.getEmployee().equals(employee)) {
                        Json.save(file.getPath(), this);
                    }
                }
            }
        }
    }
}