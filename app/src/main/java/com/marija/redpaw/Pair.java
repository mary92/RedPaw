package com.marija.redpaw;

/*
    Needed to keep track of each animal's shelter.
     */
public class Pair{
    public Animal animal;
    public Shelter shelter;

    public Pair(Animal animal, Shelter shelter){
        this.animal=animal;
        this.shelter=shelter;
    }
}
