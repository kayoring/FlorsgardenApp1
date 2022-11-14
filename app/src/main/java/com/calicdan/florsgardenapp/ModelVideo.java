package com.calicdan.florsgardenapp;

public class ModelVideo {
    String id, title, timestamp, videoUri;

    public ModelVideo(){

    }

    public ModelVideo(String id, String title, String timestamp, String videoUri) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.videoUri = videoUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
