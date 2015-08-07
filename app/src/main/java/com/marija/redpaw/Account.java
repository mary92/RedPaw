package com.marija.redpaw;

import java.io.Serializable;

/**
 * Created by demouser on 8/6/15.
 */
public class Account implements Serializable{
    private String uid;
    private String shelterId;

    public Account(){}

    public Account(String uid, String shelterId) {
        this.uid = uid;
        this.shelterId = shelterId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }
}
