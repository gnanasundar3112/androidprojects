package com.sundar.i_macbankers.Models;

public class selectBoxModel {
    private String id;
    private String name;

    public selectBoxModel(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return name; // This is used by the ArrayAdapter to display the customer name in the ListView
    }
}
