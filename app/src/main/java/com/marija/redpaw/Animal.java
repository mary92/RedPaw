package com.marija.redpaw;

import android.graphics.Bitmap;
import android.view.animation.AnticipateInterpolator;

import java.io.Serializable;

/**
 * Container class for an animal object.
 */
public class Animal implements Serializable {
    private String description;
    private Type type;
    private String img;

    //Empty constructor
    public Animal(){}

    public Animal(String description, Type type, String img ){
        this.description=description;
        this.type=type;
        this.img=img;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public Type getType(){
        return type;
    }

    public void setType(Type type){
        this.type=type;
    }

    public String getImg(){
        return img;
    }

    public void setImg(String img){
        this.img=img;
    }

}
