package com.demoapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response {

    @SerializedName("venues")
    private ArrayList<Venue> venuesCatList;

    @SerializedName("categories")
    private ArrayList<Category> venuesList;

    public ArrayList<Category> getVenueArrayList() {
        return venuesList;
    }

    public void setVenueArrayList(ArrayList<Category> categoryArrayList) {
        this.venuesList = categoryArrayList;
    }


    public ArrayList<Venue> getVenuesCatList() {
        return venuesCatList;
    }

    public void setVenuesCatList(ArrayList<Venue> venuesCatList) {
        this.venuesCatList = venuesCatList;
    }
}
