package com.locanthach.sharefood.model;

/**
 * Created by An Lee on 7/16/2017.
 */

public class User {
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;

    public User() {
        this.name = "Anonymous";
        this.email = "Anonymous";
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
