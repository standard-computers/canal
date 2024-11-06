package org.Canal.Models.HumanResources;

import java.util.ArrayList;

public class Department {

    private String id; //Department ID
    private String organization; //Org this Department belongs too
    private String location;
    private String department; //If child Department, Parent Department ID
    private String name; //Name of Department
    private ArrayList<Position> positions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public void addPosition(Position position) {
        this.positions.add(position);
    }

    public Position getPosition(int position) {
        return this.positions.get(position);
    }

    public Position getPosition(String id) {
        for(Position p : this.positions) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}