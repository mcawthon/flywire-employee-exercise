package com.flywire.exercise.model;

import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String position;
    private boolean active;
    private List<Integer> directReports;
    private String hireDate;
    private String manager;
    private String lastName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Integer> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Integer> directReports) {
        this.directReports = directReports;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getLastName() {
        String[] parts = this.name.split(" ");
        return parts[parts.length - 1];
    }

    public boolean isActive() {
        return this.active;
    }
}