package com.locanthach.sharefood.entity;

import com.locanthach.sharefood.model.Comment;
import com.locanthach.sharefood.model.Photo;
import com.locanthach.sharefood.model.User;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by An Lee on 7/16/2017.
 */

public class PostEntity extends RealmObject {
    public String id;
    public String title;
    public String location;
    public String timeStamp;
    public List<Photo> photos;
    public List<Comment> comments;
    public int likeCount;
    public boolean liked;
    public User user;

    public PostEntity() {
    }

    public PostEntity(String id, String title, String location, String timeStamp,
                      List<Photo> photos, List<Comment> comments, int likeCount, boolean liked,
                      User user) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.timeStamp = timeStamp;
        this.photos = photos;
        this.comments = comments;
        this.likeCount = likeCount;
        this.liked = liked;
        this.user = user;
    }
}
