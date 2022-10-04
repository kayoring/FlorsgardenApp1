package com.calicdan.florsgardenapp.Model;

public class User {

    private String id;
    private String fullName;
    private String imageURL;
    private String status;
    private String search;

    public User(String id, String fullName, String imageURL) {
        this.id = id;
        this.fullName = fullName;
        this.imageURL = imageURL;
        //this.status = status;
        //this.search = search;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return fullName;
    }

    public void setUsername(String username) {
        this.fullName = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}