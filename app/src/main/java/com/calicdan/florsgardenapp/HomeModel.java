package com.calicdan.florsgardenapp;

public class HomeModel {
    String name;
    String description;
    String Surl;

//constructor
    HomeModel() {


    }
    public HomeModel(String name, String description, String Surl) {
        this.name = name;
        this.description = description;
        this.Surl = Surl;
    }
//setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSurl() {
        return Surl;
    }

    public void setSurl(String Surl) {
        this.Surl = Surl;
    }
}
