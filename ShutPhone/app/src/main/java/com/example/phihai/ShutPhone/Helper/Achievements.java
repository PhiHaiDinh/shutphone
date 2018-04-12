package com.example.phihai.ShutPhone.Helper;

/**
 * Created by Phi Hai on 27.12.2017.
 */

public class Achievements {

    private int id;
    private String name;
    private String description;
    private int gotPoints;

    public Achievements(int id, String name, String description, int gotPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gotPoints = gotPoints;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGotPoints(int gotPoints) {
        this.gotPoints = gotPoints;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getGotPoints() {
        return gotPoints;
    }
}
