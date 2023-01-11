package com.calicdan.florsgardenapp.Model;

public class Inquiries
{
    public String username, id, imageURL, question, time, date, userType;


    public Inquiries(){
        //required empty contructor
    }

    public Inquiries(String username, String id, String imageURL, String question, String time, String date, String userType) {
        this.username = username;
        this.id = id;
        this.imageURL = imageURL;
        this.question = question;
        this.time = time;
        this.date = date;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsernamee() {
        return username;
    }

    public void setUsernamee(String usernamee) {
        this.username = usernamee;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String profileImage) {
        this.imageURL = profileImage;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
