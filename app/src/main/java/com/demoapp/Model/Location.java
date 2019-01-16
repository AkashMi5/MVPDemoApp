package com.demoapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Location {

    @SerializedName("address")
    private String address;
    @SerializedName("distance")
    private String distance;

    @SerializedName("formattedAddress")
    private ArrayList<String> formattedAddress;

    public Location(String address, String distance){
        this.address=address;
        this.distance=distance;
    }

    public ArrayList<String> getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(ArrayList<String> formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
