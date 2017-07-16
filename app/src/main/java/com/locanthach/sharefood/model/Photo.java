package com.locanthach.sharefood.model;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Photo {
    private String id;
    private String url;


    public Photo() {
    }

    public Photo(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
