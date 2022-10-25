package com.calicdan.florsgardenapp.Model;

public class Inquiries
{
    public String usernamee, uid, imageURL, question, time, date;


    public Inquiries(){
        //required empty contructor
    }

    public Inquiries(String usernamee, String uid, String imageURL, String question, String time, String date) {
        this.usernamee = usernamee;
        this.uid = uid;
        this.imageURL = imageURL;
        this.question = question;
        this.time = time;
        this.date = date;
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
        return usernamee;
    }

    public void setUsernamee(String usernamee) {
        this.usernamee = usernamee;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
