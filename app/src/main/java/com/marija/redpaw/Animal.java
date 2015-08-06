package com.marija.redpaw;

import android.graphics.Bitmap;
import android.view.animation.AnticipateInterpolator;

/**
 * Container class for an animal object.
 */
public class Animal {
    private String description;
    private Type type;
    private Bitmap img;

    //Empty constructor
    public Animal(){}

    public Animal(String description, Type type, Bitmap img ){
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

    public Bitmap getImg(){
        return img;
    }

    public void setImg(Bitmap img){
        this.img=img;
    }

}
