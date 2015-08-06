package com.marija.redpaw;

/**
 * Created by demouser on 8/6/15.
 */
public class Account {
    private String username;
    private String password;
    private String shelterId;

    public Account(){}

    public Account(String username, String password, String shelterId) {
        this.username = username;
        this.password = password;
        this.shelterId = shelterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }
}
