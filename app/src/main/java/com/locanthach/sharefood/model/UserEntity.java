package com.locanthach.sharefood.model;

import io.realm.RealmObject;

/**
 * Created by An Lee on 7/30/2017.
 */

public class UserEntity extends RealmObject {
    public String id;
    public String name;
    public String email;
    public String profileImageUrl;

    public UserEntity() {
    }

    public UserEntity(String id, String name, String email, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
