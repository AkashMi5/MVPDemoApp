package com.demoapp.Model;

import android.graphics.drawable.Icon;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Category {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("pluralName")
    private String pluralName;
    @SerializedName("shortName")
    private String shortName;

    @SerializedName("categories")
    private ArrayList<SubCategory> subCategoryList;


    public Category(String id, String name, String pluralName, String shortName){
        this.id=id;
        this.name=name;
        this.pluralName=pluralName;
        this.shortName=shortName;
    }

    public ArrayList<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
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

    public String getPluralName() {
        return pluralName;
    }
    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


}
