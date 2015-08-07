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
    private double latitude;
    private double longitude;
    private Status status;
    private Date timestamp;

    /*private static int lastId = 0;

    private int id = lastId++;
*/
    public Animal getAnimal() {
        return animal;
    }



    public void setAnimal(Animal animal) {
        this.animal = animal;
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
        this.setLatitude(location.getLatitude());
        this.setLongitude(location.getLongitude());
        status=Status.REPORTED;
        timestamp=new Date();
    }

    public Report(String description, Type type, String img, Location location) {
        animal = new Animal(description, type, img);
        this.setLatitude(location.getLatitude());
        this.setLongitude(location.getLongitude());
        status=Status.REPORTED;
        timestamp=new Date();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
