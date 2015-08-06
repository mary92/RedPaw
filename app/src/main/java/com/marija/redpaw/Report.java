package com.marija.redpaw;

import android.graphics.Bitmap;
import android.location.Location;

import com.firebase.client.core.Repo;

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
        status=Status.REPORTED;
        timestamp=new Date();
    };

    public Report(Animal animal, Location location){
        this.setAnimal(animal);
        this.setLocation(location);
        status=Status.REPORTED;
        timestamp=new Date();
    }

    public Report(String description, Type type, Bitmap img, Location location) {
        animal = new Animal(description, type, img);
        this.setLocation(location);
        status=Status.REPORTED;
        timestamp=new Date();
    }
}
