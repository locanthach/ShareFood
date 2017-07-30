package com.locanthach.sharefood.model;

import io.realm.RealmObject;

public class Like extends RealmObject {
    public String key;
    public String value;

    public Like() {
    }

    public Like(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
