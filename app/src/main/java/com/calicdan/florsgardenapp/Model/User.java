package com.calicdan.florsgardenapp.Model;

public class User {

    private String id;
    private String fullName;
    private String imageURL;
    private String status;
    private String search;
    private String email;
    private String contact;
    private String address;
    private String password;
    private String usertype;


    public User(String id, String fullName, String imageURL, String status,
                String search, String email, String contact, String address, String password, String usertype) {
        this.id = id;
        this.fullName = fullName;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.password = password;
        this.usertype = usertype;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}