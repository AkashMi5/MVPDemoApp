package com.demoapp.Model;

import com.google.gson.annotations.SerializedName;

public class Venue {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private Location location;

    public Venue(String id, String name){
        this.id=id;
        this.name=name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

}
