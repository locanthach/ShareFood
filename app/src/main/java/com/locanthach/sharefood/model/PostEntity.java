package com.locanthach.sharefood.model;

import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by An Lee on 7/30/2017.
 */

public class PostEntity extends RealmObject {
    public String id;
    public String uid;
    public String author;
    public String content;
    public String photoUrl;
    public String location;
    public String time;
    public String likeCount;
    public String status;
    public String viewCount;
    public RealmList<Like> likes;

    public PostEntity() {
    }

    public PostEntity(String id, String uid, String author, String content, String photoUrl, String location, String time, String likeCount, String status, String viewCount, Map<String, Boolean> likes) {
        this.id = id;
        this.uid = uid;
        this.author = author;
        this.content = content;
        this.photoUrl = photoUrl;
        this.location = location;
        this.time = time;
        this.likeCount = likeCount;
        this.status = status;
        this.viewCount = viewCount;
        this.likes = new RealmList<>();
        for (String currentKey : likes.keySet()) {
            Like like = new Like(currentKey, String.valueOf(likes.get(currentKey)));
            this.likes.add(like);
        }
    }
}

