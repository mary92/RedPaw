package com.marija.redpaw;

import android.location.Location;

import java.util.Date;

/**
 * Created by demouser on 8/6/15.
 */
public class Report {
    private Animal animal;
    private Location location;
    private Status status;
    private Date timestamp;

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Report(){
        setStatus(Status.REPORTED);
    };

    public Report(Animal animal, Location location, Status status, Date timestamp){
        this.setAnimal(animal);
        this.setLocation(location);
        this.setStatus(status);
        this.setTimestamp(timestamp);
    }
}
