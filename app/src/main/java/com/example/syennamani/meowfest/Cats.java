package com.example.syennamani.meowfest;

/**
 * Created by syennamani on 1/24/2018.
 */

public class Cats {

    private String title;
    private String timestamp;
    private String image_url;
    private String description;

    public Cats() {
    }

    public Cats(String title, String timestamp, String image_url, String description) {
        this.title = title;
        this.timestamp = timestamp;
        this.image_url = image_url;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
